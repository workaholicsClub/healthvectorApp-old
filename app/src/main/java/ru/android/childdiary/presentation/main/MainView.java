package ru.android.childdiary.presentation.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseActivityView;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface MainView extends BaseActivityView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void childListLoaded(List<Child> childList);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setActive(@Nullable Child child);

    void addChild();

    void editChild(@Nullable Child child);

    void reviewChild(@NonNull Child child);
}
