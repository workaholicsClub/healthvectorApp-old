package ru.android.healthvector.domain.calendar.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.core.requests.DeleteResponse;
import ru.android.healthvector.domain.calendar.data.ExerciseEvent;

@Value
@Builder
public class UpdateExerciseEventResponse implements DeleteResponse {
    @NonNull
    UpdateExerciseEventRequest request;
    @NonNull
    ExerciseEvent exerciseEvent;
    @NonNull
    List<String> imageFilesToDelete;
}
