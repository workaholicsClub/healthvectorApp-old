package ru.android.childdiary.presentation.events.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalDate;

import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseView;

public interface EventDetailView<T extends MasterEvent> extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showChild(@NonNull Child child);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showDate(@NonNull LocalDate date);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showEventDetail(@NonNull T event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void eventAdded(@NonNull T event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void eventUpdated(@NonNull T event);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showDefaultNotifyTime(int minutes);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showNotifyTimeDialog(@NonNull Child child);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showFoodMeasureDialog(@NonNull Child child);
}
