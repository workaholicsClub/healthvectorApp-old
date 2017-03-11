package ru.android.childdiary.presentation.events.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.presentation.core.BaseView;

public interface EventDetailView<EventType extends MasterEvent> extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showEventDetail(@NonNull EventType event);
}
