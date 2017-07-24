package ru.android.childdiary.presentation.chart.antropometry.pages.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.chart.core.ChartView;

public interface AntropometryChartView extends ChartView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showResults(@NonNull AntropometryChartState state);
}
