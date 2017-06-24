package ru.android.childdiary.presentation.cloud;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.cloud.core.CloudView;

public interface CloudOperationView extends CloudView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void setIsAuthorized(boolean isAuthorized);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMain();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void restartApp();
}
