package ru.android.childdiary.presentation.chart.testing.pages.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.chart.core.ChartView;
import ru.android.childdiary.presentation.chart.testing.dialogs.ParameterDialogArguments;

public interface DomanChartView extends ChartView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showResults(@NonNull DomanChartState state);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void specifyParameter(@NonNull ParameterDialogArguments arguments);
}
