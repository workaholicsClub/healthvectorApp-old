package ru.android.childdiary.presentation.medical.partitions.core;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.core.BaseView;

public interface BaseMedicalDataView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showDeletingEvents(boolean loading);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void noChildSpecified();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showNoDataToFilter();
}
