package ru.android.childdiary.domain.interactors.exercises.requests;

import android.support.annotation.Nullable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.exercises.ConcreteExercise;

@Value
@Builder
public class DeleteConcreteExerciseEventsRequest {
    @NonNull
    ConcreteExercise concreteExercise;
    @Nullable
    Integer linearGroup;
}
