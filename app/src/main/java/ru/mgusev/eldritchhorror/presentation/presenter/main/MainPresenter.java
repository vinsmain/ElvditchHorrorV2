package ru.mgusev.eldritchhorror.presentation.presenter.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.app.App;
import ru.mgusev.eldritchhorror.auth.GoogleAuth;
import ru.mgusev.eldritchhorror.database.oldDB.DatabaseHelperOld;
import ru.mgusev.eldritchhorror.model.Game;
import ru.mgusev.eldritchhorror.presentation.view.main.MainView;
import ru.mgusev.eldritchhorror.repository.Repository;
import ru.mgusev.eldritchhorror.support.CircularTransformation;

import static android.content.ContentValues.TAG;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private static final int MIN_SORT_MODE = 1;
    private static final int MAX_SORT_MODE = 5;

    @Inject
    Repository repository;
    @Inject
    GoogleAuth googleAuth;
    private List<Game> gameList;
    private CompositeDisposable gameListSubscribe;
    private CompositeDisposable userIconSubscribe;
    private CompositeDisposable authSubscribe;
    private CompositeDisposable rateSubscribe;
    private String tempIconUrl;

    private int deletingGamePosition;

    public MainPresenter() {
        App.getComponent().inject(this);
        gameListSubscribe = new CompositeDisposable();
        userIconSubscribe = new CompositeDisposable();
        authSubscribe = new CompositeDisposable();
        rateSubscribe = new CompositeDisposable();
        gameListSubscribe.add(repository.getGameListPublish().subscribe(this::updateGameList));
        userIconSubscribe.add(repository.getUserIconPublish().subscribe(this::updateUserIcon));
        authSubscribe.add(repository.getAuthPublish().subscribe(this::showAuthError));
        rateSubscribe.add(repository.getRatePublish().subscribe(this::showRateDialog));
        tempIconUrl = googleAuth.getDefaultIconUrl();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        importOldUserData();
        gameList = repository.getGameList(0, 0);
        repository.gameListOnNext();
        if (googleAuth.getCurrentUser() != null) auth();
    }



    private void importOldUserData() {
        new DatabaseHelperOld(repository.getContext()); //import data from old version User DB
        repository.getContext().deleteDatabase("eldritchHorrorDB.db"); //delete old version User DB
        repository.getContext().deleteDatabase("EHLocalDB.db"); //delete old version Static DB
    }

    public void startUpdateUserIcon() {
        updateUserIcon(tempIconUrl);
    }

    private void updateUserIcon(String iconUrl) {
        tempIconUrl = iconUrl;
        if (!iconUrl.equals(googleAuth.getDefaultIconUrl())) {
            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    Log.d("DEBUG", "onBitmapLoaded");
                    BitmapDrawable mBitmapDrawable = new BitmapDrawable(repository.getContext().getResources(), bitmap);
                    getViewState().setUserIcon(mBitmapDrawable);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    Log.d("DEBUG", "onBitmapFailed");
                    getViewState().setUserIcon(repository.getContext().getResources().getDrawable(R.drawable.google_signed_in));
                }

                @Override
                public void onPrepareLoad(Drawable drawable) {
                    Log.d("DEBUG", "onPrepareLoad");
                    getViewState().setUserIcon(repository.getContext().getResources().getDrawable(R.drawable.google_signed_in));
                }
            };

            Picasso.get()
                    .load(iconUrl)
                    .transform(new CircularTransformation())
                    .into(target);
        } else getViewState().setUserIcon(repository.getContext().getResources().getDrawable(R.drawable.google_icon));
    }

    public void setSortModeIcon() {
        getViewState().setSortIcon(repository.getSortMode());
    }

    public void addGame() {
        getViewState().intentToPager();
    }

    public void setCurrentGame(int position) {
        repository.setGame(gameList.get(position));
    }

    private void updateGameList(List<Game> gameList) {
        this.gameList = new ArrayList<>();
        this.gameList.addAll(gameList);
        showEmptyListMessage();
        setVisibilityStatisticsMenuItem();
        updateStatistics();
        getViewState().setDataToAdapter(this.gameList);
    }

    private void showEmptyListMessage() {
        if (gameList.isEmpty()) getViewState().showEmptyListMessage();
        else getViewState().hideEmptyListMessage();
    }

    public void setVisibilityStatisticsMenuItem() {
        if (gameList.isEmpty()) getViewState().hideStatisticsMenuItem();
        else getViewState().showStatisticsMenuItem();
    }

    private void updateStatistics() {
        if (repository.getVictoryGameCount(0) == 0) getViewState().setStatistics(repository.getGameCount(0));
        else getViewState().setStatistics(repository.getGameCount(0), repository.getBestScore(), repository.getWorstScore());
    }

    public void showDeleteDialog(int deletingGamePosition) {
        this.deletingGamePosition = deletingGamePosition;
        getViewState().showDeleteDialog();
    }

    public void hideDeleteDialog() {
        getViewState().hideDeleteDialog();
    }

    private void showRateDialog(boolean value) {
        getViewState().showRateDialog();
    }

    public void hideRateDialog() {
        getViewState().hideRateDialog();
    }

    public void setRateResult(boolean isRate) {
        repository.setRate(isRate);
    }

    public void deleteGame() {
        repository.deleteGame(gameList.get(deletingGamePosition));
        gameList.remove(deletingGamePosition);
        getViewState().deleteGame(deletingGamePosition, gameList);
        showEmptyListMessage();
        setVisibilityStatisticsMenuItem();
        updateStatistics();
    }

    public void changeSortMode() {
        if (repository.getSortMode() == MAX_SORT_MODE) repository.setSortMode(MIN_SORT_MODE);
        else repository.setSortMode(repository.getSortMode() + 1);
        getViewState().setSortIcon(repository.getSortMode());
        repository.gameListOnNext();
    }


    public void actionAuth() {
        if (googleAuth.getCurrentUser() != null) getViewState().showSignOutMenu();
        else auth();
    }

    public void auth() {
        getViewState().signIn(googleAuth.getGoogleSignInClient().getSignInIntent());
    }

    public void signOut() {
        googleAuth.signOut();
    }

    public void startAuthTask(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            googleAuth.firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e);
            tempIconUrl = googleAuth.getDefaultIconUrl();
            getViewState().setUserIcon(repository.getContext().getResources().getDrawable(R.drawable.google_icon));
            getViewState().showErrorSnackBar();
        }
    }

    private void showAuthError(boolean isAuthFail) {
        if (isAuthFail) getViewState().showErrorSnackBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gameListSubscribe.dispose();
        userIconSubscribe.dispose();
        authSubscribe.dispose();
        rateSubscribe.dispose();
    }
}