package ru.android.childdiary.presentation.splash;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.presentation.core.BaseView;

public interface SplashView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void startApp();
}
