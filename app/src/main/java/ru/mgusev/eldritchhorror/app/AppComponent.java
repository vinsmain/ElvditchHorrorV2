package ru.mgusev.eldritchhorror.app;

import javax.inject.Singleton;

import dagger.Component;
import ru.mgusev.eldritchhorror.adapter.DetailsAdapter;
import ru.mgusev.eldritchhorror.adapter.InvestigatorChoiceAdapter;
import ru.mgusev.eldritchhorror.adapter.MainAdapter;
import ru.mgusev.eldritchhorror.adapter.StatisticsAdapter;
import ru.mgusev.eldritchhorror.database.FirebaseHelper;
import ru.mgusev.eldritchhorror.presentation.presenter.details.DetailsPresenter;
import ru.mgusev.eldritchhorror.presentation.presenter.main.MainPresenter;
import ru.mgusev.eldritchhorror.presentation.presenter.pager.ExpansionChoicePresenter;
import ru.mgusev.eldritchhorror.presentation.presenter.pager.InvestigatorChoicePresenter;
import ru.mgusev.eldritchhorror.presentation.presenter.pager.InvestigatorPresenter;
import ru.mgusev.eldritchhorror.presentation.presenter.pager.PagerPresenter;
import ru.mgusev.eldritchhorror.presentation.presenter.pager.ResultGamePresenter;
import ru.mgusev.eldritchhorror.presentation.presenter.pager.StartDataPresenter;
import ru.mgusev.eldritchhorror.presentation.presenter.statistics.StatisticsPresenter;

@Component (modules = AppModule.class)
@Singleton
public interface AppComponent {
    void inject(MainPresenter presenter);
    void inject(DetailsPresenter presenter);
    void inject(StatisticsPresenter presenter);
    void inject(PagerPresenter presenter);
    void inject(StartDataPresenter presenter);
    void inject(InvestigatorChoicePresenter presenter);
    void inject(ResultGamePresenter presenter);
    void inject(InvestigatorPresenter presenter);
    void inject(ExpansionChoicePresenter presenter);

    void inject(MainAdapter adapter);
    void inject(InvestigatorChoiceAdapter adapter);
    void inject(DetailsAdapter adapter);
    void inject(StatisticsAdapter adapter);

    void inject(FirebaseHelper helper);
}