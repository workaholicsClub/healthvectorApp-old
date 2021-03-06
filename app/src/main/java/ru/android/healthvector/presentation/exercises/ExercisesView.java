package ru.android.healthvector.presentation.exercises;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalTime;

import java.util.List;

import ru.android.healthvector.domain.exercises.data.ConcreteExercise;
import ru.android.healthvector.domain.exercises.data.Exercise;
import ru.android.healthvector.presentation.core.AppPartitionView;

public interface ExercisesView extends AppPartitionView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showExercises(@NonNull List<Exercise> exercisesState);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToExercise(@NonNull ExerciseDetailState state);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToConcreteExerciseAdd(@NonNull ConcreteExercise defaultConcreteExercise,
                                       @Nullable LocalTime startTime,
                                       @Nullable LocalTime finishTime);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void startLoading();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void stopLoading();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void noChildSpecified();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToProfileAdd();
}
