package ru.android.childdiary.domain.interactors.calendar.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;

@Value
@Builder
public class UpdateMedicineTakingEventRequest {
    @NonNull
    MedicineTakingEvent medicineTakingEvent;
}
