package ru.android.childdiary.domain.interactors.exercises;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.exercises.ExerciseDataRepository;
import ru.android.childdiary.domain.interactors.exercises.requests.UpsertConcreteExerciseRequest;
import ru.android.childdiary.domain.interactors.exercises.requests.UpsertConcreteExerciseResponse;

public class ExerciseInteractor {
    private final ExerciseRepository exerciseRepository;

    @Inject
    public ExerciseInteractor(ExerciseDataRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Observable<Exercise> getExercise(@NonNull Exercise exercise) {
        return exerciseRepository.getExercise(exercise);
    }

    public Observable<List<Exercise>> getExercises() {
        return exerciseRepository.getExercises();
    }

    public Observable<List<Exercise>> updateExercisesIfNeeded() {
        return exerciseRepository.updateExercisesIfNeeded();
    }

    public Observable<UpsertConcreteExerciseResponse> addConcreteExercise(@NonNull UpsertConcreteExerciseRequest request) {
        return null;//validate(request)
        //.flatMap(this::createImageFile)
        //.flatMap(doctorVisitRepository::addDoctorVisit)
        //.flatMap(this::postprocess);
    }
}
