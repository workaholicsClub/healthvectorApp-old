package ru.android.childdiary.presentation.profile;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.presentation.core.BaseView;

public interface ProfileEditView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setButtonDoneEnabled(boolean enabled);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void validationFailed();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showValidationErrorMessage(String msg);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void nameValidated(boolean valid);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void sexValidated(boolean valid);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void birthDateValidated(boolean valid);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void birthHeightValidated(boolean valid);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void birthWeightValidated(boolean valid);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void childAdded(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void childUpdated(@NonNull Child child);
}
