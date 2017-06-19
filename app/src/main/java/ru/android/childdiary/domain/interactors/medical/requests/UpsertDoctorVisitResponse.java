package ru.android.childdiary.domain.interactors.medical.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.core.DeleteResponse;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;

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