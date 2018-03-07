package ru.android.healthvector.presentation.exercises;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalTime;

import ru.android.healthvector.domain.exercises.data.ConcreteExercise;
import ru.android.healthvector.presentation.core.BaseView;

public interface ExerciseDetailView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showExercise(@NonNull ExerciseDetailState state);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToConcreteExerciseAdd(@NonNull ConcreteExercise defaultConcreteExercise,
                                       @Nullable LocalTime startTime,
                                       @Nullable LocalTime finishTime);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToWebBrowser(@NonNull String title, @NonNull String url);
}
