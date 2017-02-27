package ru.android.childdiary.presentation.splash;

import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.presentation.core.BaseActivityView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SplashView extends BaseActivityView {
    void startApp(@Nullable Sex sex);
}
