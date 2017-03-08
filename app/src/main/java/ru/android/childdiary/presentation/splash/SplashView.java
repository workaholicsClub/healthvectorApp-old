package ru.android.childdiary.presentation.splash;

import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseView;

public interface SplashView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void startApp(@Nullable Child child);
}
