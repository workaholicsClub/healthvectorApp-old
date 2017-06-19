package ru.android.childdiary.presentation.events;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.calendar.events.ExerciseEvent;
import ru.android.childdiary.presentation.events.core.EventDetailView;

public interface ExerciseEventDetailView extends EventDetailView<ExerciseEvent> {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void exerciseEventNameValidated(boolean valid);
}
