package ru.android.healthvector.domain.medical.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.core.requests.DeleteResponse;

@Value
@Builder
public class DeleteDoctorVisitEventsResponse implements DeleteResponse {
    @NonNull
    DeleteDoctorVisitEventsRequest request;
    int count;
    @NonNull
    List<String> imageFilesToDelete;
}
