package ru.android.childdiary.presentation.exercises;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.exercises.data.ConcreteExercise;
import ru.android.childdiary.presentation.core.events.BaseAddItemView;

public interface AddConcreteExerciseView extends BaseAddItemView<ConcreteExercise> {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void concreteExerciseNameValidated(boolean valid);
}
