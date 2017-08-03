package ru.android.childdiary.domain.interactors.exercises;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.exercises.data.Exercise;
import ru.android.childdiary.domain.interactors.exercises.requests.UpsertConcreteExerciseRequest;
import ru.android.childdiary.domain.interactors.exercises.requests.UpsertConcreteExerciseResponse;

public interface ExerciseRepository {
    Observable<Exercise> getExercise(@NonNull Exercise exercise);

    Observable<List<Exercise>> getExercises(@NonNull Child child);

    Observable<List<Exercise>> updateExercises();

    Observable<List<Exercise>> updateExercisesIfNeeded();

    Single<Boolean> hasConnectedEvents(@NonNull Exercise exercise);

    Observable<UpsertConcreteExerciseResponse> addConcreteExercise(@NonNull UpsertConcreteExerciseRequest request);
}
