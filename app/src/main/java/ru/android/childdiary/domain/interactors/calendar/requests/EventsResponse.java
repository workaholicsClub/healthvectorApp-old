package ru.android.childdiary.domain.interactors.calendar.requests;

import java.util.List;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;

@Value
@Builder
public class EventsResponse {
    EventsRequest request;
    List<MasterEvent> events;
}
