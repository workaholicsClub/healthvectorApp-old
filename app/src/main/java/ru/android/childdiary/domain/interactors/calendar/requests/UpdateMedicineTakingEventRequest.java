package ru.android.childdiary.domain.interactors.calendar.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.calendar.data.MedicineTakingEvent;
import ru.android.childdiary.domain.interactors.calendar.data.core.LinearGroupFieldType;

@Value
@Builder
public class UpdateMedicineTakingEventRequest {
    @NonNull
    MedicineTakingEvent medicineTakingEvent;
    @NonNull
    List<LinearGroupFieldType> fields;
    int minutes;
}
