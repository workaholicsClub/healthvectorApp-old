package ru.android.childdiary.domain.medical.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.core.requests.DeleteResponse;

@Value
@Builder
public class DeleteDoctorVisitEventsResponse implements DeleteResponse {
    @NonNull
    DeleteDoctorVisitEventsRequest request;
    int count;
    @NonNull
    List<String> imageFilesToDelete;
}
