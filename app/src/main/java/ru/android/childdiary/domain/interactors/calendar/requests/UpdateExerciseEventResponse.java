package ru.android.childdiary.domain.interactors.calendar.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.core.requests.DeleteResponse;
import ru.android.childdiary.domain.interactors.calendar.data.ExerciseEvent;

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
