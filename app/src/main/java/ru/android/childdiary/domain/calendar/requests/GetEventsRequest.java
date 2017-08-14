package ru.android.childdiary.domain.calendar.requests;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.child.data.Child;

@Value
@Builder
public class GetEventsRequest {
    @NonNull
    Child child;
    @NonNull
    LocalDate date;
    @NonNull
    LocalTime time;
    @NonNull
    GetEventsFilter filter;
}
