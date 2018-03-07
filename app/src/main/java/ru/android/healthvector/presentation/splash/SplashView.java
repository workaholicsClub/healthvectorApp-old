package ru.android.healthvector.presentation.splash;

import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.presentation.core.BaseView;

public interface SplashView extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMain(@Nullable Sex sex);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToAppIntro(@Nullable Sex sex);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToCloud(@Nullable Sex sex);
}
