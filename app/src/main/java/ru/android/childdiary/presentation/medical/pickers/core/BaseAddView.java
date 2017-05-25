package ru.android.childdiary.presentation.medical.pickers.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.presentation.core.BaseView;

public interface BaseAddView<T> extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void itemAdded(@NonNull T item);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showList(@NonNull List<T> list);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setButtonDoneEnabled(boolean enabled);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void validationFailed();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showValidationErrorMessage(String msg);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void nameValidated(boolean valid);
}
