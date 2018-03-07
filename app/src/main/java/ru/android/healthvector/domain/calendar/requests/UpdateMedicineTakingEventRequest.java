package ru.android.healthvector.domain.calendar.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.calendar.data.MedicineTakingEvent;
import ru.android.healthvector.domain.calendar.data.core.LinearGroupFieldType;

@Value
@Builder
public class UpdateMedicineTakingEventRequest {
    @NonNull
    MedicineTakingEvent medicineTakingEvent;
    @NonNull
    List<LinearGroupFieldType> fields;
    int minutes;
}
