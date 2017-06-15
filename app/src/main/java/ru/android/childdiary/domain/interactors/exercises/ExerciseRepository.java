package ru.android.childdiary.domain.interactors.exercises;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;

public interface ExerciseRepository {
    Observable<Exercise> getExercise(@NonNull Exercise exercise);

    Observable<List<Exercise>> getExercises();

    Observable<List<Exercise>> updateExercisesIfNeeded();
}
