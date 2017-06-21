package ru.android.childdiary.presentation.cloud;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.core.BaseView;

public interface CloudView extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void requestPermission();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void chooseAccount();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void acquireGooglePlayServices();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void connectionUnavailable();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showBackupLoading(boolean loading);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMain();
}
