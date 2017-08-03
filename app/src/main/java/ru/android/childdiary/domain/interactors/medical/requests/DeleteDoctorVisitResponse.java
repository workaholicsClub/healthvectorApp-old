package ru.android.childdiary.domain.interactors.medical.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.core.requests.DeleteResponse;

@Value
@Builder
public class DeleteDoctorVisitResponse implements DeleteResponse {
    @NonNull
    DeleteDoctorVisitRequest request;
    @NonNull
    List<String> imageFilesToDelete;
}
