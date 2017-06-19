package ru.android.childdiary.presentation.exercises;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalTime;

import ru.android.childdiary.domain.interactors.exercises.ConcreteExercise;
import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.presentation.core.BaseView;

public interface ExerciseDetailView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showExercise(@NonNull Exercise exercise);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToConcreteExerciseAdd(@NonNull ConcreteExercise defaultConcreteExercise,
                                       @Nullable LocalTime startTime,
                                       @Nullable LocalTime finishTime);
}
