package ru.android.childdiary.presentation.events.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.presentation.core.BaseView;

public interface EventDetailView<T extends MasterEvent> extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showEventDetail(@NonNull T event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void eventAdded(@NonNull T event, boolean afterButtonPressed);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void eventUpdated(@NonNull T event, boolean afterButtonPressed);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void eventDeleted(@NonNull MasterEvent event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void eventDone(boolean done);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void validationFailed();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showValidationErrorMessage(String msg);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setButtonAddEnabled(boolean enabled);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void confirmDeleteOneEvent(@NonNull MasterEvent event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void askDeleteOneEventOrLinerGroup(@NonNull MasterEvent event);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showDeletingEvents(boolean loading);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showUpdatingEvents(boolean loading);
}
