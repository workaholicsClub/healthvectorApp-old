package ru.android.childdiary.presentation.events.core;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;
import java.util.Map;

import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.calendar.data.core.TimeUnit;

public interface PeriodicEventDetailView<T extends MasterEvent> extends EventDetailView<T> {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showLengthValueDialog(@NonNull Map<TimeUnit, List<Integer>> timeUnitValues);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void eventsAdded(int count);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showGeneratingEvents(boolean loading);
}
