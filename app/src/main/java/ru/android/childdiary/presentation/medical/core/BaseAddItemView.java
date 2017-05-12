package ru.android.childdiary.presentation.medical.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.io.Serializable;

public interface BaseAddItemView<T extends Serializable> extends BaseItemView<T> {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setLoading(boolean loading);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void added(@NonNull T item);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setButtonDoneEnabled(boolean enabled);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void validationFailed();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showValidationErrorMessage(String msg);
}
