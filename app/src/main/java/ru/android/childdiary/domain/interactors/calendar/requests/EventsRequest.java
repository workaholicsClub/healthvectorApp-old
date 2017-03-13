package ru.android.childdiary.domain.interactors.calendar.requests;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@Builder
public class EventsRequest {
    Child child;
    LocalDate date;
}
