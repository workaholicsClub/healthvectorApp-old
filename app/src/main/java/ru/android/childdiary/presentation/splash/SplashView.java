package ru.android.childdiary.presentation.splash;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.core.BaseView;

public interface SplashView extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMain();
}
