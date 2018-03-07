package ru.android.healthvector.domain.exercises.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.exercises.data.ConcreteExercise;

@Value
@Builder(toBuilder = true)
public class UpsertConcreteExerciseRequest {
    @NonNull
    ConcreteExercise concreteExercise;
}
