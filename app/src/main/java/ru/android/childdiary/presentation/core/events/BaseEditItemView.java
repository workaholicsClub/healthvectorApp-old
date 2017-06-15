package ru.android.childdiary.presentation.core.events;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.DateTime;

import java.io.Serializable;

public interface BaseEditItemView<T extends Serializable> extends BaseItemView<T> {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void updated(@NonNull T item, int count);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void deleted(@NonNull T item);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void completed(@NonNull T item);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void askDeleteConnectedEventsOrNot(@NonNull T item);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void confirmDeleteOneItem(@NonNull T item);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void askCompleteFromDate(@NonNull T item, @NonNull DateTime dateTime);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showDeletingEvents(boolean loading);
}
