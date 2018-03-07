package ru.android.healthvector.presentation.calendar.partitions;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.Set;

import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.domain.calendar.data.DoctorVisitEvent;
import ru.android.healthvector.domain.calendar.data.ExerciseEvent;
import ru.android.healthvector.domain.calendar.data.MedicineTakingEvent;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.domain.calendar.data.standard.DiaperEvent;
import ru.android.healthvector.domain.calendar.data.standard.FeedEvent;
import ru.android.healthvector.domain.calendar.data.standard.OtherEvent;
import ru.android.healthvector.domain.calendar.data.standard.PumpEvent;
import ru.android.healthvector.domain.calendar.data.standard.SleepEvent;
import ru.android.healthvector.presentation.core.BaseView;

public interface BaseCalendarView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showCalendarState(CalendarState calendarState);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDiaperEvent(@NonNull MasterEvent event, @NonNull DiaperEvent defaultEvent);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToFeedEvent(@NonNull MasterEvent event, @NonNull FeedEvent defaultEvent);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToOtherEvent(@NonNull MasterEvent event, @NonNull OtherEvent defaultEvent);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToPumpEvent(@NonNull MasterEvent event, @NonNull PumpEvent defaultEvent);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSleepEvent(@NonNull MasterEvent event, @NonNull SleepEvent defaultEvent);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDoctorVisitEvent(@NonNull MasterEvent event, @NonNull DoctorVisitEvent defaultEvent);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToMedicineTakingEvent(@NonNull MasterEvent event, @NonNull MedicineTakingEvent defaultEvent);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToExerciseEvent(@NonNull MasterEvent event, @NonNull ExerciseEvent defaultEvent);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void confirmDeleteOneEvent(@NonNull MasterEvent event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void askDeleteOneEventOrLinerGroup(@NonNull MasterEvent event);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showDeletingEvents(boolean loading);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showUpdatingEvents(boolean loading);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showFilterDialog(@NonNull Set<EventType> eventTypes);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void noChildSpecified();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToProfileAdd();
}
