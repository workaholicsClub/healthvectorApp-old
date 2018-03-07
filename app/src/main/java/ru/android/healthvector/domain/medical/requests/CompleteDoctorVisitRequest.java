package ru.android.healthvector.domain.medical.requests;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.medical.data.DoctorVisit;

@Value
@Builder
public class CompleteDoctorVisitRequest {
    @NonNull
    DoctorVisit doctorVisit;
    @NonNull
    DateTime dateTime;
    boolean delete;
}
