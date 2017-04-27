package ru.android.childdiary.presentation.events.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseView;
import ru.android.childdiary.presentation.events.dialogs.TimeDialog;

public interface EventDetailView<T extends MasterEvent> extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showDefaultEventDetail(@NonNull T event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showEventDetail(@NonNull T event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void eventAdded(@NonNull T event, boolean afterButtonPressed);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void eventUpdated(@NonNull T event, boolean afterButtonPressed);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void eventDeleted(@NonNull MasterEvent event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void eventDone(@NonNull MasterEvent event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showTimeDialog(String tag, @NonNull Child child, TimeDialog.Parameters parameters);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void validationFailed();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showValidationErrorMessage(String msg);
}
