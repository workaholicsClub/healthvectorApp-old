package ru.android.childdiary.presentation.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalDate;

import ru.android.childdiary.domain.child.data.Child;

public interface AppPartitionView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showSelectedDate(@NonNull LocalDate selectedDate);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showChild(@NonNull Child child);
}
