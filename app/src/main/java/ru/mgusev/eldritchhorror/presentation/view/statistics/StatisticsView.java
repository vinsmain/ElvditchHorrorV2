package ru.mgusev.eldritchhorror.presentation.view.statistics;

import com.github.mikephil.charting.data.PieEntry;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.SkipStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import ru.mgusev.eldritchhorror.model.Investigator;

public interface StatisticsView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void initAncientOneSpinner(List<String> ancientOneList);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setItemSelected(int position);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setBackgroundImage(String imageResource);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setDefaultBackgroundImage();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setInvestigatorList(List<Investigator> list);

    @StateStrategyType(SkipStrategy.class)
    void initFragments();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setBestResult(String value);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setLastResult(String value);

    @StateStrategyType(SkipStrategy.class)
    void goToDetailsActivity();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setDataToResultChart(List<PieEntry> entries, String description, List<String> labels, List<Float> values, int sum);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setDataToAncientOneChart(List<PieEntry> entries, String description, List<String> labels, List<Float> values, int sum);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setVisibilityAncientOneChart(boolean isVisible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setDataToScoreChart(List<PieEntry> entries, String description, List<String> labels, List<Float> values, int sum);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setVisibilityScoreChart(boolean isVisible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setDataToDefeatReasonChart(List<PieEntry> entries, String description, List<String> labels, List<Float> values, int sum);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setVisibilityDefeatReasonChart(boolean isVisible);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setDataToInvestigatorChart(List<PieEntry> entries, String description, List<String> labels, List<Float> values, int sum);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void setVisibilityInvestigatorChart(boolean isVisible);

    @StateStrategyType(SkipStrategy.class)
    void invalidateView();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void finishActivity();
}