package ru.android.childdiary.presentation.core.events;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.io.Serializable;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseView;
import ru.android.childdiary.presentation.core.fields.dialogs.TimeDialog;

public interface BaseAddItemView<T extends Serializable> extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void added(@NonNull T item);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showTimeDialog(String tag, @NonNull Child child, TimeDialog.Parameters parameters);
}
