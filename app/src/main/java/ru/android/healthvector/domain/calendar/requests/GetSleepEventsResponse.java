package ru.android.healthvector.domain.calendar.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.calendar.data.standard.SleepEvent;

@Value
@Builder
public class GetSleepEventsResponse {
    @NonNull
    GetSleepEventsRequest request;
    @NonNull
    List<SleepEvent> events;
}
