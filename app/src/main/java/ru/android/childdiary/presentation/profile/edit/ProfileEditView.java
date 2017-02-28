package ru.android.childdiary.presentation.profile.edit;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseActivityView;

public interface ProfileEditView extends BaseActivityView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setButtonDoneEnabled(boolean enabled);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void validationFailed();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showValidationErrorMessage(String msg);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void nameValidated(boolean valid, boolean shouldFocus);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void sexValidated(boolean valid, boolean shouldFocus);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void birthDateValidated(boolean valid, boolean shouldFocus);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void birthHeightValidated(boolean valid, boolean shouldFocus);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void birthWeightValidated(boolean valid, boolean shouldFocus);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void childAdded(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void childUpdated(@NonNull Child child);
}
