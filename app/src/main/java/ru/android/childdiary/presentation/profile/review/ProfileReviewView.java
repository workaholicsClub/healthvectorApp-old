package ru.android.childdiary.presentation.profile.review;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseActivityView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ProfileReviewView extends BaseActivityView {
    void childLoaded(@NonNull Child child);
}
