package ru.android.childdiary.domain.exercises;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.domain.calendar.data.core.LengthValue;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.exercises.data.ConcreteExercise;
import ru.android.childdiary.domain.exercises.data.Exercise;
import ru.android.childdiary.domain.exercises.requests.UpsertConcreteExerciseRequest;
import ru.android.childdiary.domain.exercises.requests.UpsertConcreteExerciseResponse;

public interface ExerciseRepository {
    Observable<Exercise> getExercise(@NonNull Exercise exercise);

    Observable<List<Exercise>> getExercises(@NonNull Child child);

    Observable<List<Exercise>> updateExercises();

    Observable<List<Exercise>> updateExercisesIfNeeded();

    Single<Boolean> hasConnectedEvents(@NonNull Exercise exercise);

    Observable<UpsertConcreteExerciseResponse> addConcreteExercise(@NonNull UpsertConcreteExerciseRequest request);

    Observable<Integer> continueLinearGroup(@NonNull ConcreteExercise concreteExercise,
                                            @NonNull LocalDate sinceDate,
                                            @NonNull Integer linearGroup,
                                            @NonNull LengthValue lengthValue);
}
