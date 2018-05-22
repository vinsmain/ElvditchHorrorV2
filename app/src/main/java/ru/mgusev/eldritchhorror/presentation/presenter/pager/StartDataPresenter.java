package ru.mgusev.eldritchhorror.presentation.presenter.pager;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.mgusev.eldritchhorror.app.App;
import ru.mgusev.eldritchhorror.model.AncientOne;
import ru.mgusev.eldritchhorror.model.Expansion;
import ru.mgusev.eldritchhorror.model.Game;
import ru.mgusev.eldritchhorror.model.Prelude;
import ru.mgusev.eldritchhorror.presentation.view.pager.StartDataView;
import ru.mgusev.eldritchhorror.repository.Repository;

@InjectViewState
public class StartDataPresenter extends MvpPresenter<StartDataView> {

    public static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    @Inject
    Repository repository;
    private Calendar date;
    private List<String> playersCountList;
    private CompositeDisposable expansionSubscribe;
    private CompositeDisposable randomSubscribe;

    private AncientOne currentAncientOne;
    private Prelude currentPrelude;
    private int currentPlayersCount;

    private List<AncientOne> ancientOneList;
    private List<Prelude> preludeList;


    public StartDataPresenter() {
        App.getComponent().inject(this);

        expansionSubscribe = new CompositeDisposable();
        expansionSubscribe.add(repository.getExpansionPublish().subscribe(this::initSpinners));
        randomSubscribe = new CompositeDisposable();
        randomSubscribe.add(repository.getRandomPublish().subscribe(this::setRandomValues));
        date = Calendar.getInstance();
        playersCountList = repository.getPlayersCountArray();
        ancientOneList = repository.getAncientOneList();
        preludeList = repository.getPreludeList();

        initCurrentValues();

        date.setTimeInMillis(repository.getGame().getDate());
    }

    private void initCurrentValues() {
        currentAncientOne = repository.getAncientOne(repository.getGame().getAncientOneID());
        currentPrelude = repository.getPrelude(repository.getGame().getPreludeID());
        currentPlayersCount = repository.getGame().getPlayersCount();
    }

    private void initSpinners(List<Expansion> expansionList) {
        setSpinnerPosition();
    }

    private void setRandomValues(int position) {
        if (position == 0) {
            getRandomAncientOne();
            getRandomPrelude();
            setSpinnerPosition();
        }
    }

    private void getRandomAncientOne() {
        String randomName;
        do {
            randomName = getAncientOneNameList().get(new Random().nextInt(getAncientOneNameList().size()));
        } while (repository.getAncientOne(randomName).getId() == currentAncientOne.getId());
        currentAncientOne = repository.getAncientOne(randomName);
    }

    private void getRandomPrelude() {
        String randomName;
        do {
            randomName = getPreludeNameList().get(new Random().nextInt(getPreludeNameList().size()));
        } while (getPreludeNameList().size() != 1 && repository.getPrelude(randomName).getId() == currentPrelude.getId());
        currentPrelude = repository.getPrelude(randomName);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        setDateToField();
        setSpinnerPosition();
        setSwitchValue();
    }

    private List<String> getAncientOneNameList() {
        List<String> list = new ArrayList<>();
        for (AncientOne ancientOne : ancientOneList) {
            if (repository.getExpansion(ancientOne.getExpansionID()).isEnable() || (currentAncientOne != null && ancientOne.getId() == currentAncientOne.getId()))
                list.add(ancientOne.getName());
        }
        if (currentAncientOne == null) currentAncientOne = repository.getAncientOne(list.get(0));
        return list;
    }

    private List<String> getPreludeNameList() {
        List<String> list = new ArrayList<>();
        for (Prelude prelude : preludeList) {
            if (repository.getExpansion(prelude.getExpansionID()).isEnable() || prelude.getId() == currentPrelude.getId())
                list.add(prelude.getName());
        }
        return list;
    }

    public void showDatePicker() {
        getViewState().showDatePicker(date);
    }

    public void setDate(int year, int month, int day) {
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, day);
        repository.getGame().setDate(date.getTimeInMillis());
    }

    public void setDateToField() {
        getViewState().setDateToField(date);
    }

    public void dismissDatePicker() {
        getViewState().dismissDatePicker();
    }

    public void setCurrentPlayersCount(String value) {
        currentPlayersCount = Integer.parseInt(value);
    }

    public void setCurrentAncientOne(String value) {
        currentAncientOne = repository.getAncientOne(value);
        repository.ancientOneOnNext(currentAncientOne);
    }

    public void setCurrentPrelude(String value) {
        currentPrelude = repository.getPrelude(value);
    }

    public void setSpinnerPosition() {
        getViewState().initAncientOneSpinner(getAncientOneNameList());
        getViewState().initPreludeSpinner(getPreludeNameList());
        getViewState().initPlayersCountSpinner(playersCountList);
        getViewState().setSpinnerPosition(getAncientOneNameList().indexOf(currentAncientOne.getName()),
                getPreludeNameList().indexOf(currentPrelude.getName()), playersCountList.indexOf(String.valueOf(currentPlayersCount)));
    }

    public void setSwitchValue() {
        Game game = repository.getGame();
        getViewState().setSwitchValue(game.isSimpleMyths(), game.isNormalMyths(), game.isHardMyths(), game.isStartingRumor());
    }

    public void setSwitchValueToGame(boolean easyMythosValue, boolean normalMythosValue, boolean hardMythosValue, boolean startingRumorValue) {
        repository.getGame().setSimpleMyths(easyMythosValue);
        repository.getGame().setNormalMyths(normalMythosValue);
        repository.getGame().setHardMyths(hardMythosValue);
        repository.getGame().setStartingRumor(startingRumorValue);
    }

    @Override
    public void onDestroy() {
        repository.getGame().setPlayersCount(currentPlayersCount);
        repository.getGame().setAncientOneID(currentAncientOne.getId());
        repository.getGame().setPreludeID(currentPrelude.getId());
        //repository.clearGame();
        expansionSubscribe.dispose();
        randomSubscribe.dispose();
        super.onDestroy();
    }
}