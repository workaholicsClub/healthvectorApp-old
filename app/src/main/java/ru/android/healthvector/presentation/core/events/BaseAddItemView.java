package ru.android.healthvector.presentation.core.events;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.io.Serializable;

public interface BaseAddItemView<T extends Serializable> extends BaseItemView<T> {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void added(@NonNull T item, int count);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setButtonDoneEnabled(boolean enabled);
}
