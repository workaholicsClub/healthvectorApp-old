package ru.android.childdiary.presentation.exercises;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.presentation.core.AppPartitionView;

public interface ExercisesView extends AppPartitionView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showExercises(@NonNull List<Exercise> exercisesState);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToExercise(@NonNull ExerciseDetailState state);
}
