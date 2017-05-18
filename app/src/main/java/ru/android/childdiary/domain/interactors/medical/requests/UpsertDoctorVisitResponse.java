package ru.android.childdiary.domain.interactors.medical.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;

@Value
@Builder
public class UpsertDoctorVisitResponse {
    @NonNull
    UpsertDoctorVisitRequest request;
    int addedEventsCount;
    @NonNull
    DoctorVisit doctorVisit;
}
