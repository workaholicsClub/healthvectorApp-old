package ru.android.childdiary.presentation.profile.review;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseActivityView;

public interface ProfileReviewView extends BaseActivityView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showChild(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void editChild(@NonNull Child child);
}
