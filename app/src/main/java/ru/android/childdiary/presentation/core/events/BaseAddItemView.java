package ru.android.childdiary.presentation.core.events;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.io.Serializable;

public interface BaseAddItemView<T extends Serializable> extends BaseItemView<T> {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void added(@NonNull T item);
}
