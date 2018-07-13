package ru.mgusev.eldritchhorror.repository;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.Module;
import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.database.FirebaseHelper;
import ru.mgusev.eldritchhorror.database.staticDB.StaticDataDB;
import ru.mgusev.eldritchhorror.database.userDB.UserDataDB;
import ru.mgusev.eldritchhorror.model.AncientOne;
import ru.mgusev.eldritchhorror.model.Expansion;
import ru.mgusev.eldritchhorror.model.Game;
import ru.mgusev.eldritchhorror.model.Investigator;
import ru.mgusev.eldritchhorror.model.Localization;
import ru.mgusev.eldritchhorror.model.Prelude;
import ru.mgusev.eldritchhorror.app.AppModule;
import ru.mgusev.eldritchhorror.model.StatisticsInvestigator;

@Module (includes = AppModule.class)
public class Repository {

    private final Context context;
    private final PublishSubject<AncientOne> ancientOnePublish;
    private final PublishSubject<Integer> scorePublish;
    private final PublishSubject<Boolean> isWinPublish;
    private final StaticDataDB staticDataDB;
    private final UserDataDB userDataDB;
    private final PrefHelper prefHelper;

    private Investigator investigator;
    private PublishSubject<Investigator> investigatorPublish;
    private PublishSubject<List<Expansion>> expansionPublish;
    private PublishSubject<Integer> randomPublish;
    private PublishSubject<Integer> clearPublish;
    private PublishSubject<List<Game>> gameListPublish;
    private PublishSubject<String> userIconPublish;
    private final PublishSubject<Boolean> authPublish;

    private CompositeDisposable firebaseSubscribe;

    private Game game;
    private int pagerPosition = 0;
    private FirebaseHelper firebaseHelper;
    private FirebaseUser user;

    @Inject
    public Repository(Context context, StaticDataDB staticDataDB, UserDataDB userDataDB, PrefHelper prefHelper, FirebaseHelper firebaseHelper) {
        this.context = context;
        this.staticDataDB = staticDataDB;
        this.userDataDB = userDataDB;
        this.prefHelper = prefHelper;
        this.firebaseHelper = firebaseHelper;
        ancientOnePublish = PublishSubject.create();
        scorePublish = PublishSubject.create();
        isWinPublish = PublishSubject.create();
        investigatorPublish = PublishSubject.create();
        expansionPublish = PublishSubject.create();
        randomPublish = PublishSubject.create();
        clearPublish = PublishSubject.create();
        gameListPublish = PublishSubject.create();
        userIconPublish = PublishSubject.create();
        authPublish = PublishSubject.create();
    }

    public Game getGame() {
        if (game == null) game = new Game();
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        this.game.setInvList(userDataDB.investigatorDAO().getByGameID(game.getId()));
    }

    public PublishSubject<String> getUserIconPublish() {
        return userIconPublish;
    }

    public void userIconOnNext(String iconUrl) {
        userIconPublish.onNext(iconUrl);
    }

    public int getPagerPosition() {
        return pagerPosition;
    }

    public void setPagerPosition(int pagerPosition) {
        this.pagerPosition = pagerPosition;
    }

    public Context getContext() {
        return context;
    }

    public void clearGame() {
        game = null;
    }

    public Observable<AncientOne> getObservableAncientOne() {
        return ancientOnePublish;
    }

    public void ancientOneOnNext(AncientOne ancientOne) {
        ancientOnePublish.onNext(ancientOne);
    }

    public Observable<Integer> getObservableScore() {
        return scorePublish;
    }

    public void scoreOnNext() {
        scorePublish.onNext(game.getScore());
    }

    public Observable<Integer> getRandomPublish() {
        return randomPublish;
    }

    public void randomOnNext(int position) {
        randomPublish.onNext(position);
    }

    public Observable<Integer> getClearPublish() {
        return clearPublish;
    }

    public void clearOnNext(int position) {
        clearPublish.onNext(position);
    }

    public Observable<Boolean> getObservableIsWin() {
        return isWinPublish;
    }

    public void isWinOnNext() {
        isWinPublish.onNext(game.getIsWinGame());
    }

    public AncientOne getAncientOne(int id) {
        return staticDataDB.ancientOneDAO().getAncientOneByID(id);
    }

    // StartDataFragment

    public List<String> getPlayersCountArray() {
        return Arrays.asList(context.getResources().getStringArray(R.array.playersCountArray));
    }

    public List<AncientOne> getAncientOneList() {
        if (Localization.getInstance().isRusLocale()) return staticDataDB.ancientOneDAO().getAllRU();
        else return staticDataDB.ancientOneDAO().getAllEN();
    }

    public List<Prelude> getPreludeList() {
        if (Localization.getInstance().isRusLocale()) return staticDataDB.preludeDAO().getAllRU();
        else return staticDataDB.preludeDAO().getAllEN();
    }

    public Prelude getPrelude(int id) {
        return staticDataDB.preludeDAO().getPreludeByID(id);
    }

    public AncientOne getAncientOne(String name) {
        return staticDataDB.ancientOneDAO().getAncientOneByName(name);
    }

    public Prelude getPrelude(String name) {
        return staticDataDB.preludeDAO().getPreludeByName(name);
    }

    // InvestigatorChoiceFragment

    public List<Investigator> getInvestigatorList() {
        if (Localization.getInstance().isRusLocale()) return staticDataDB.investigatorDAO().getAllRU();
        else return staticDataDB.investigatorDAO().getAllEN();
    }

    public List<Expansion> getExpansionList() {
        return staticDataDB.expansionDAO().getAll();
    }

    public Expansion getExpansion(int id) {
        return staticDataDB.expansionDAO().getExpansionByID(id);
    }

    public PublishSubject<Investigator> getInvestigatorPublish() {
        return investigatorPublish;
    }

    public void investigatorOnNext() {
        investigatorPublish.onNext(investigator);
    }

    public Investigator getInvestigator() {
        return investigator;
    }

    public void setInvestigator(Investigator investigator) {
        this.investigator = investigator;
    }

    // ExpansionChoiceActivity

    public void saveExpansionList(List<Expansion> list) {
        for (Expansion expansion : list) {
            staticDataDB.expansionDAO().updateExpansion(expansion);
        }
    }

    public PublishSubject<List<Expansion>> getExpansionPublish() {
        return expansionPublish;
    }

    public void expansionOnNext() {
        expansionPublish.onNext(getExpansionList());
    }

    // Game

    public int getSortMode() {
        return prefHelper.loadSortMode();
    }

    public void setSortMode(int sortMode) {
        prefHelper.saveSortMode(sortMode);
    }

    public List<Game> getGameList(int sortMode, int ancientOneId) {
        switch (sortMode == 0 ? prefHelper.loadSortMode() : sortMode) {
            case 1:
                return ancientOneId == 0 ? userDataDB.gameDAO().getGameListSortedDateDescending() : userDataDB.gameDAO().getGameListSortedDateDescending(ancientOneId);
            case 2:
                return userDataDB.gameDAO().getGameListSortedDateAscending();
            case 3:
                return userDataDB.gameDAO().getGameListSortedAncientOne();
            case 4:
                return ancientOneId == 0 ? userDataDB.gameDAO().getGameListSortedScoreAscending() : userDataDB.gameDAO().getGameListSortedScoreAscending(ancientOneId);
            case 5:
                return userDataDB.gameDAO().getGameListSortedScoreDescending();
            default:
                return userDataDB.gameDAO().getGameListSortedDateDescending();
        }
    }

    public void initFirebaseHelper() {
        firebaseHelper.initReference(user);
        firebaseSubscribe = new CompositeDisposable();
        firebaseSubscribe.add(firebaseHelper.getChildEventDisposable().subscribe(this::processDataFromFirebase));
        for (Game game : userDataDB.gameDAO().getGameListSortedDateAscending()) {
            if (game.getUserID() == null) insertGame(game);
        }
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    private void processDataFromFirebase(RxFirebaseChildEvent<Game> data) {
        switch (data.getEventType()) {
            case ADDED:
                changeGame(data.getValue());
                break;
            case CHANGED:
                changeGame(data.getValue());
                break;
            case REMOVED: {
                removeGame(data.getValue());
                break;
            }
        }
    }

    public void firebaseSubscribeDispose() {
        firebaseSubscribe.dispose();
    }

    private void changeGame(Game game) {
        if (this.game != null && this.game.getId() == game.getId()) this.game = game;
        if (getGameById(game.getId()) == null || game.getLastModified() != getGameById(game.getId()).getLastModified()) {
            insertGameToDB(game);
            gameListOnNext();
        }
    }

    private void removeGame(Game game) {
        if (game != null) {
            deleteGameFromDB(game);
            gameListOnNext();
        }
    }

    public void insertGame(Game game) {
        long time = new Date().getTime();
        game.setLastModified(time);
        for (Investigator investigator : game.getInvList()) {
            investigator.setGameId(game.getId());
            investigator.setId(time++);
        }
        if (user != null && game.getUserID() == null) {
            game.setUserID(user.getUid());
        }
        insertGameToDB(game);
        firebaseHelper.addGame(game);
    }

    private void insertGameToDB(Game game) {
        userDataDB.gameDAO().insertGame(game);
        userDataDB.investigatorDAO().deleteByGameID(game.getId());
        userDataDB.investigatorDAO().insertInvestigatorList(game.getInvList());
    }

    public void deleteGame(Game game) {
        deleteGameFromDB(game);
        Toast.makeText(context, R.string.success_deleting_message, Toast.LENGTH_SHORT).show();
        firebaseHelper.removeGame(game);
    }

    private void deleteGameFromDB(Game game) {
        userDataDB.gameDAO().deleteGame(game);
        userDataDB.investigatorDAO().deleteByGameID(game.getId());
    }

    public void deleteSynchGames() {
        for (Game game : userDataDB.gameDAO().getGameListSortedDateAscending()) {
            if (game.getUserID() != null) {
                deleteGameFromDB(game);
            }
        }
    }

    public PublishSubject<List<Game>> getGameListPublish() {
        return gameListPublish;
    }

    public void gameListOnNext() {
        gameListPublish.onNext(getGameList(0, 0));
    }

    // Statistics

    public int getGameCount(int ancientOneId) {
        return ancientOneId ==0 ? userDataDB.gameDAO().getGameCount() : userDataDB.gameDAO().getGameCount(ancientOneId);
    }

    public int getVictoryGameCount(int ancientOneId) {
        return ancientOneId == 0 ? userDataDB.gameDAO().getVictoryGameCount() : userDataDB.gameDAO().getVictoryGameCount(ancientOneId);
    }

    public int getDefeatGameCount(int ancientOneId) {
        return ancientOneId == 0 ? userDataDB.gameDAO().getDefeatGameCount() : userDataDB.gameDAO().getDefeatGameCount(ancientOneId);
    }

    public int getBestScore() {
        return userDataDB.gameDAO().getBestScore();
    }

    public int getWorstScore() {
        return userDataDB.gameDAO().getWorstScore();
    }

    public List<AncientOne> getAddedAncientOneList() {
        return Localization.getInstance().isRusLocale() ? staticDataDB.ancientOneDAO().getAllRU(userDataDB.gameDAO().getAncientOneIdList()) : staticDataDB.ancientOneDAO().getAllEN(userDataDB.gameDAO().getAncientOneIdList());
    }

    public int getAncientOneCountById(int id) {
        return userDataDB.gameDAO().getAncientOneCountByID(id);
    }

    public List<Integer> getScoreList(int ancientOneId) {
        return ancientOneId == 0 ? userDataDB.gameDAO().getScoreList() : userDataDB.gameDAO().getScoreList(ancientOneId);
    }

    public int getScoreCount(int score, int ancientOneId) {
        return ancientOneId == 0 ? userDataDB.gameDAO().getScoreCount(score) : userDataDB.gameDAO().getScoreCount(score, ancientOneId);
    }

    public int getDefeatByEliminationCount(int ancientOneId) {
        return ancientOneId == 0 ? userDataDB.gameDAO().getDefeatByEliminationCount() : userDataDB.gameDAO().getDefeatByEliminationCount(ancientOneId);
    }

    public int getDefeatByMythosDepletionCount(int ancientOneId) {
        return ancientOneId == 0 ? userDataDB.gameDAO().getDefeatByMythosDepletionCount() : userDataDB.gameDAO().getDefeatByMythosDepletionCount(ancientOneId);
    }

    public int getDefeatByAwakenedAncientOneCount(int ancientOneId) {
        return ancientOneId == 0 ? userDataDB.gameDAO().getDefeatByAwakenedAncientOneCount() : userDataDB.gameDAO().getDefeatByAwakenedAncientOneCount(ancientOneId);
    }

    public List<StatisticsInvestigator> getStatisticsInvestigatorList(int ancientOneId) {
        return userDataDB.investigatorDAO().getStatisticsInvestigatorList(ancientOneId == 0 ? userDataDB.gameDAO().getGameIdList() : userDataDB.gameDAO().getGameIdList(ancientOneId));
    }

    public Investigator getInvestigator(String name) {
        return staticDataDB.investigatorDAO().getByName(name);
    }

    private Game getGameById(long id) {
        return userDataDB.gameDAO().getGame(id);
    }

    public PublishSubject<Boolean> getAuthPublish() {
        return authPublish;
    }

    public void authOnNext(boolean isAuthFail) {
        authPublish.onNext(isAuthFail);
    }
}