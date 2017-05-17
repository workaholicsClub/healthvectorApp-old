package ru.android.childdiary.domain.interactors.calendar.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.calendar.events.standard.SleepEvent;

@Value
@Builder
public class GetSleepEventsResponse {
    @NonNull
    GetSleepEventsRequest request;
    @NonNull
    List<SleepEvent> events;
}
