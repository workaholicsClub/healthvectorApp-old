package ru.android.healthvector.domain.calendar.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.core.requests.DeleteResponse;
import ru.android.healthvector.domain.calendar.data.DoctorVisitEvent;

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
