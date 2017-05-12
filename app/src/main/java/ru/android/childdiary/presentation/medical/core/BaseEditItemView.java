package ru.android.childdiary.presentation.medical.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.io.Serializable;

public interface BaseEditItemView<T extends Serializable> extends BaseItemView<T> {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void updated(@NonNull T item);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void deleted(@NonNull T item);
}
