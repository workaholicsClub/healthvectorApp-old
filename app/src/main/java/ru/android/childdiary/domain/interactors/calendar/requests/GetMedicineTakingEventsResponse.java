package ru.android.childdiary.domain.interactors.calendar.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.calendar.events.MedicineTakingEvent;

@Value
@Builder
public class GetMedicineTakingEventsResponse {
    @NonNull
    GetMedicineTakingEventsRequest request;
    @NonNull
    List<MedicineTakingEvent> events;
}
