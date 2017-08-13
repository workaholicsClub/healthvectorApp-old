package ru.android.childdiary.domain.medical.requests;

import android.support.annotation.Nullable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.medical.data.DoctorVisit;

@Value
@Builder
public class DeleteDoctorVisitEventsRequest {
    @NonNull
    DoctorVisit doctorVisit;
    @Nullable
    Integer linearGroup;
}
