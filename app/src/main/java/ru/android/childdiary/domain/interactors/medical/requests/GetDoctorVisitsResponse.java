package ru.android.childdiary.domain.interactors.medical.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.data.DoctorVisit;

@Value
@Builder
public class GetDoctorVisitsResponse {
    @NonNull
    GetDoctorVisitsRequest request;
    @NonNull
    List<DoctorVisit> doctorVisits;
}
