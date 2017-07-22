package ru.android.childdiary.presentation.development.partitions.antropometry.core;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.core.BaseView;

public interface AntropometryView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void validationFailed();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showValidationErrorMessage(String msg);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void heightWeightValidated(boolean valid);
}
