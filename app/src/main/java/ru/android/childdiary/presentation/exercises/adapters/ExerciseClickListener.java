package ru.android.childdiary.presentation.exercises.adapters;

import android.support.annotation.NonNull;

import ru.android.childdiary.domain.interactors.exercises.Exercise;

public interface ExerciseClickListener {
    void showExerciseDetails(@NonNull Exercise exercise);

    void addConcreteExercise(@NonNull Exercise exercise);
}
