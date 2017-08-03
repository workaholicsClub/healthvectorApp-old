package ru.android.childdiary.domain.interactors.calendar.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.calendar.data.ExerciseEvent;
import ru.android.childdiary.domain.interactors.calendar.data.core.LinearGroupFieldType;

@Value
@Builder
public class UpdateExerciseEventRequest {
    @NonNull
    ExerciseEvent exerciseEvent;
    @NonNull
    List<LinearGroupFieldType> fields;
    int minutes;
}
