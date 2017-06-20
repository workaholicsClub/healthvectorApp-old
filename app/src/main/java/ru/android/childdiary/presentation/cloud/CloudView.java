package ru.android.childdiary.presentation.cloud;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.core.BaseView;

public interface CloudView extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMain();
}
