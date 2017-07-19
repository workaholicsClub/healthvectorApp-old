package ru.android.childdiary.presentation.testing.dynamic;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.core.BaseView;

public interface DomanChartView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showResults(@NonNull DomanChartState state);
}
