package ru.android.childdiary.domain.interactors.medical.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;

@Value
@Builder(toBuilder = true)
public class UpsertDoctorVisitRequest {
    @NonNull
    DoctorVisit doctorVisit;
}
