package ru.android.childdiary.presentation.calendar.partitions;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.Set;

import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.events.ExerciseEvent;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.presentation.core.BaseView;

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

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showNeedToFillNoteOrPhoto(@NonNull MasterEvent event);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showDeletingEvents(boolean loading);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showUpdatingEvents(boolean loading);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showFilterDialog(@NonNull Set<EventType> eventTypes);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void noChildSpecified();
}
