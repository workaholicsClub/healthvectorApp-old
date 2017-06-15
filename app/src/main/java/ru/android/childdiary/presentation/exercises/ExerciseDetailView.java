package ru.android.childdiary.presentation.exercises;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.android.childdiary.domain.interactors.exercises.Exercise;
import ru.android.childdiary.presentation.core.BaseView;

public interface ExerciseDetailView extends BaseView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showExercise(@NonNull Exercise exercise);
}
