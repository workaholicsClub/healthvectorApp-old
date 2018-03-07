package ru.android.healthvector.domain.medical.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.medical.data.DoctorVisit;

@Value
@Builder(toBuilder = true)
public class UpsertDoctorVisitRequest {
    @NonNull
    DoctorVisit doctorVisit;
}
