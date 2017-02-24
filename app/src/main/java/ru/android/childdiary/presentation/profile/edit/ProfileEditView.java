package ru.android.childdiary.presentation.profile.edit;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseActivityView;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface ProfileEditView extends BaseActivityView {
    void childAdded(Child child);

    void childUpdated(Child child);
}
