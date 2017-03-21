package ru.android.childdiary.presentation.calendar;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalDate;

import java.util.List;

import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseView;

public interface CalendarView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setActive(@NonNull Child child);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSelected(@NonNull LocalDate date);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showEvents(@NonNull LocalDate date, @NonNull List<MasterEvent> events);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToDiaperEventEdit(@NonNull MasterEvent event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToFeedEventEdit(@NonNull MasterEvent event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToOtherEventEdit(@NonNull MasterEvent event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToPumpEventEdit(@NonNull MasterEvent event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToSleepEventEdit(@NonNull MasterEvent event);
}
