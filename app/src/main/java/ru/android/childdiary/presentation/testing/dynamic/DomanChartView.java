package ru.android.childdiary.presentation.testing.dynamic;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.core.BaseView;
import ru.android.childdiary.presentation.testing.dialogs.ParameterDialogArguments;

public interface DomanChartView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showResults(@NonNull DomanChartState state);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void specifyParameter(@NonNull ParameterDialogArguments arguments);
}
