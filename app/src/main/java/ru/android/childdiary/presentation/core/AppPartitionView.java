package ru.android.childdiary.presentation.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.child.Child;

public interface AppPartitionView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showChild(@NonNull Child child);
}
