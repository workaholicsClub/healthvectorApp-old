package ru.android.childdiary.presentation.cloud;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.cloud.core.CloudView;

public interface CloudOperationView extends CloudView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMain();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showState(@NonNull CloudOperationState state);
}
