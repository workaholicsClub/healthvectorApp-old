package ru.android.healthvector.domain.exercises.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.core.requests.DeleteResponse;
import ru.android.healthvector.domain.exercises.data.ConcreteExercise;

@Value
@Builder
public class UpsertConcreteExerciseResponse implements DeleteResponse {
    @NonNull
    UpsertConcreteExerciseRequest request;
    int addedEventsCount;
    @NonNull
    ConcreteExercise concreteExercise;
    @NonNull
    List<String> imageFilesToDelete;
}
