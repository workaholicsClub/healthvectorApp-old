package ru.android.childdiary.domain.medical.requests;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.child.data.Child;

@Value
@Builder
public class GetDoctorVisitsRequest {
    @NonNull
    Child child;
    @NonNull
    GetDoctorVisitsFilter filter;
}
