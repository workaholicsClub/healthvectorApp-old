package ru.android.childdiary.presentation.main.calendar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.core.BaseView;

public interface CalendarView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setActive(@Nullable Child child);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showEvents(@NonNull List<MasterEvent> events);
}
