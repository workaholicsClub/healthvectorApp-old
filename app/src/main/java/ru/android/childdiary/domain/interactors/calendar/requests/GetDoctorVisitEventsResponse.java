package ru.android.childdiary.domain.interactors.calendar.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.calendar.data.DoctorVisitEvent;

@Value
@Builder
public class GetDoctorVisitEventsResponse {
    @NonNull
    GetDoctorVisitEventsRequest request;
    @NonNull
    List<DoctorVisitEvent> events;
}
