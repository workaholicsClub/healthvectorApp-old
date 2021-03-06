package ru.android.healthvector.domain.calendar.requests;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;

@Value
@Builder
public class GetEventsResponse {
    @NonNull
    GetEventsRequest request;
    @NonNull
    List<MasterEvent> events;
}
