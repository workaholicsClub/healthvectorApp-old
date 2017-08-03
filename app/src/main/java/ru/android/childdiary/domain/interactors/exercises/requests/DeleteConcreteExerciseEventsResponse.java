package ru.android.childdiary.domain.interactors.exercises.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.core.requests.DeleteResponse;

@Value
@Builder
public class DeleteConcreteExerciseEventsResponse implements DeleteResponse {
    @NonNull
    DeleteConcreteExerciseEventsRequest request;
    int count;
    @NonNull
    List<String> imageFilesToDelete;
}
