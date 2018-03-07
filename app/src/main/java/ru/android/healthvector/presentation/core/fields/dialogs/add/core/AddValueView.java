package ru.android.healthvector.presentation.core.fields.dialogs.add.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.healthvector.presentation.core.BaseView;

public interface AddValueView<T> extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void added(@NonNull T item);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showValidationErrorMessage(@NonNull String message);
}
