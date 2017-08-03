package ru.android.childdiary.presentation.medical;

import org.joda.time.LocalTime;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.data.DoctorVisit;

@Value
@Builder
public class DoctorVisitParameters {
    DoctorVisit defaultDoctorVisit;
    LocalTime startTime;
    LocalTime finishTime;
}
