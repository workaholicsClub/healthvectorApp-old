package ru.android.childdiary.domain.interactors.exercises;

import java.util.List;

import io.reactivex.Observable;

public interface ExerciseRepository {
    Observable<List<Exercise>> getExercises();

    Observable<List<Exercise>> updateExercisesIfNeeded();
}
