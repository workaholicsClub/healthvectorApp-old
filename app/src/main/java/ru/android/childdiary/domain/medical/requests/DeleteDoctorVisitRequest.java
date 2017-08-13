package ru.android.childdiary.domain.medical.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.medical.data.DoctorVisit;

@Value
@Builder
public class DeleteDoctorVisitRequest {
    @NonNull
    DoctorVisit doctorVisit;
}
