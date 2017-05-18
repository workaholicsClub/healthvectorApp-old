package ru.android.childdiary.domain.interactors.calendar.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;

@Value
@Builder
public class UpdateDoctorVisitEventRequest {
    @NonNull
    DoctorVisitEvent doctorVisitEvent;
}
