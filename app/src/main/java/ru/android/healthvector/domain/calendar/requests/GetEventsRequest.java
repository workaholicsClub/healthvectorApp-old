package ru.android.healthvector.domain.calendar.requests;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.domain.child.data.Child;

@Value
@Builder
public class GetEventsRequest {
    @NonNull
    Child child;
    @NonNull
    LocalDate date;
    @NonNull
    GetEventsFilter filter;
    boolean getScheduled;
}
