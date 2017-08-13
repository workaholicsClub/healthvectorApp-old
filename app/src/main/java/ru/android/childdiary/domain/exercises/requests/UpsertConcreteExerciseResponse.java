package ru.android.childdiary.domain.exercises.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.core.requests.DeleteResponse;
import ru.android.childdiary.domain.exercises.data.ConcreteExercise;

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
