package ru.mgusev.eldritchhorror.presentation.presenter.details;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.mgusev.eldritchhorror.app.App;
import ru.mgusev.eldritchhorror.model.Game;
import ru.mgusev.eldritchhorror.model.Investigator;
import ru.mgusev.eldritchhorror.presentation.view.details.DetailsView;
import ru.mgusev.eldritchhorror.repository.Repository;

@InjectViewState
public class DetailsPresenter extends MvpPresenter<DetailsView> {

    @Inject
    Repository repository;
    private Game game;
    private CompositeDisposable gameListSubscribe;
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    public DetailsPresenter() {
        App.getComponent().inject(this);
        gameListSubscribe = new CompositeDisposable();
        gameListSubscribe.add(repository.getGameListPublish().subscribe(this::initGameData));
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        initGameData(new ArrayList<>());
    }

    private void initGameData(List<Game> list) {
        game = repository.getGame();
        initHeader();
        initStartData();
        initInvestigatorList();
        initMysteriesCount();
        if (game.getIsWinGame()) initVictory();
        else initDefeat();
    }

    private void initHeader() {
        getViewState().setHeadBackground(repository.getAncientOne(game.getAncientOneID()), repository.getExpansion(repository.getAncientOne(game.getAncientOneID()).getExpansionID()));
    }

    private void initStartData() {
        getViewState().setInitialConditions(formatter.format(game.getDate()), repository.getAncientOne(game.getAncientOneID()).getName(),
                repository.getPrelude(game.getPreludeID()).getName(), String.valueOf(game.getPlayersCount()));
        getViewState().setAdditionalRules(game.getIsSimpleMyths(), game.getIsNormalMyths(), game.getIsHardMyths(), game.getIsStartingRumor());
    }

    private void initInvestigatorList() {
        List<Investigator> investigatorList = game.getInvList();
        getViewState().hideInvestigatorsNotSelected(investigatorList.isEmpty());
        getViewState().setInvestigatorsList(investigatorList);
    }

    private void initMysteriesCount() {
        getViewState().setMysteriesCount(String.valueOf(game.getSolvedMysteriesCount()));
    }

    private void initVictory() {
        getViewState().setScore(game.getScore());
        getViewState().showScore();
        getViewState().setVictoryIcon();
        getViewState().showVictoryCard();
        getViewState().setVictoryResult(game.getGatesCount(), game.getMonstersCount(), game.getCurseCount(),
                game.getRumorsCount(), game.getCluesCount(), game.getBlessedCount(), game.getDoomCount());
    }

    private void initDefeat() {
        getViewState().hideScore();
        if (game.getIsDefeatByElimination()) getViewState().setDefeatByEliminationIcon();
        else if (game.getIsDefeatByMythosDepletion()) getViewState().setDefeatByMythosDepletionIcon();
        else getViewState().setDefeatByAwakenedAncientOneIcon();
        getViewState().showDefeatCard();
        getViewState().setDefeatReason(game.getIsDefeatByElimination(), game.getIsDefeatByMythosDepletion(), game.getIsDefeatByAwakenedAncientOne());
    }

    public void showDeleteDialog() {
        getViewState().showDeleteDialog();
    }

    public void hideDeleteDialog() {
        getViewState().hideDeleteDialog();
    }

    public void deleteGame() {
        repository.deleteGame(game);
        gameListSubscribe.dispose();
        repository.gameListOnNext();
    }

    public void setCurrentPagerPosition(int position) {
        repository.setPagerPosition(position);
    }

    public void setGameToRepository() {
        repository.setGame(game);
    }

    @Override
    public void onDestroy() {
        gameListSubscribe.dispose();
        repository.clearGame();
        super.onDestroy();
    }
}