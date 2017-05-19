package ru.android.childdiary.domain.interactors.calendar.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.core.DeleteResponse;
import ru.android.childdiary.domain.interactors.calendar.events.DoctorVisitEvent;

@Value
@Builder
public class UpdateDoctorVisitEventResponse implements DeleteResponse {
    @NonNull
    UpdateDoctorVisitEventRequest request;
    @NonNull
    DoctorVisitEvent doctorVisitEvent;
    @NonNull
    List<String> imageFilesToDelete;
}
