package ru.android.childdiary.presentation.calendar.fragments;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalDate;

import java.util.List;

import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.DiaperEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.FeedEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.OtherEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.PumpEvent;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;
import ru.android.childdiary.presentation.core.AppPartitionView;

public interface BaseCalendarView extends AppPartitionView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSelectedDate(@NonNull LocalDate date);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showEvents(@NonNull LocalDate date, @NonNull List<MasterEvent> events);

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

    // TODO EXERCISE

    @StateStrategyType(OneExecutionStateStrategy.class)
    void askDeleteOneEventOrLinerGroup(@NonNull MasterEvent event);
}
