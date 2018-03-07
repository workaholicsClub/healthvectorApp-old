package ru.android.healthvector.domain.exercises.requests;

import android.support.annotation.Nullable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.exercises.data.ConcreteExercise;

@Value
@Builder
public class DeleteConcreteExerciseEventsRequest {
    @NonNull
    ConcreteExercise concreteExercise;
    @Nullable
    Integer linearGroup;
}
