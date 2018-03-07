package ru.android.healthvector.presentation.events;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.healthvector.domain.calendar.data.ExerciseEvent;
import ru.android.healthvector.presentation.events.core.PeriodicEventDetailView;

public interface ExerciseEventDetailView extends PeriodicEventDetailView<ExerciseEvent> {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void exerciseEventNameValidated(boolean valid);
}
