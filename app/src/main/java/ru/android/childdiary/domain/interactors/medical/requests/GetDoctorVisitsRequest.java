package ru.android.childdiary.domain.interactors.medical.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@Builder
public class GetDoctorVisitsRequest {
    @NonNull
    Child child;
    @NonNull
    GetDoctorVisitsFilter filter;
}
