package ru.android.childdiary.presentation.core;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface BaseView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void onUnexpectedError(Throwable e);
}
