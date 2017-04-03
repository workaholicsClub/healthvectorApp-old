package ru.android.childdiary.presentation.calendar.fragments;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalDate;

import java.util.List;

import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.presentation.core.AppPartitionView;

public interface BaseCalendarView extends AppPartitionView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSelectedDate(@NonNull LocalDate date);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showEvents(@NonNull LocalDate date, @NonNull List<MasterEvent> events);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDiaperEvent(@NonNull MasterEvent event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToFeedEvent(@NonNull MasterEvent event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToOtherEvent(@NonNull MasterEvent event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToPumpEvent(@NonNull MasterEvent event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSleepEvent(@NonNull MasterEvent event);
}
