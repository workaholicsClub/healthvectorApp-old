package ru.android.healthvector.domain.medical.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.core.requests.DeleteResponse;
import ru.android.healthvector.domain.medical.data.DoctorVisit;

@Value
@Builder
public class UpsertDoctorVisitResponse implements DeleteResponse {
    @NonNull
    UpsertDoctorVisitRequest request;
    int addedEventsCount;
    @NonNull
    DoctorVisit doctorVisit;
    @NonNull
    List<String> imageFilesToDelete;
}
