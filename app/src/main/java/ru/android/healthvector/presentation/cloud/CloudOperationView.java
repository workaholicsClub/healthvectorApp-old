package ru.android.healthvector.presentation.cloud;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.healthvector.presentation.cloud.core.CloudView;

public interface CloudOperationView extends CloudView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void setIsAuthorized(boolean isAuthorized);
}
