package ru.android.childdiary.domain.interactors.medical.requests;

import java.util.List;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;

@Value
@Builder
public class DoctorVisitsResponse {
    DoctorVisitsRequest request;
    List<DoctorVisit> doctorVisits;
}
