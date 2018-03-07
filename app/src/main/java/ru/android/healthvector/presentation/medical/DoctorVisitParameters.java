package ru.android.healthvector.presentation.medical;

import org.joda.time.LocalTime;

import lombok.Builder;
import lombok.Value;
import ru.android.healthvector.domain.medical.data.DoctorVisit;

@Value
@Builder
public class DoctorVisitParameters {
    DoctorVisit defaultDoctorVisit;
    LocalTime startTime;
    LocalTime finishTime;
}
