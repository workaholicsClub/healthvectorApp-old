package ru.android.childdiary.domain.interactors.exercises.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.exercises.data.ConcreteExercise;

@Value
@Builder(toBuilder = true)
public class UpsertConcreteExerciseRequest {
    @NonNull
    ConcreteExercise concreteExercise;
}
