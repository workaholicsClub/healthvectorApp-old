package ru.android.healthvector.domain.medical.requests;

import android.support.annotation.Nullable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.medical.data.DoctorVisit;

@Value
@Builder
public class DeleteDoctorVisitEventsRequest {
    @NonNull
    DoctorVisit doctorVisit;
    @Nullable
    Integer linearGroup;
}
