package ru.android.childdiary.domain.interactors.exercises;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.exercises.ExerciseDataRepository;

public class ExerciseInteractor {
    private final ExerciseRepository exerciseRepository;

    @Inject
    public ExerciseInteractor(ExerciseDataRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Observable<List<Exercise>> getExercises() {
        return exerciseRepository.getExercises();
    }

    public Observable<List<Exercise>> updateExercisesIfNeeded() {
        return exerciseRepository.updateExercisesIfNeeded();
    }
}
