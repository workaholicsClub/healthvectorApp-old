package ru.android.childdiary.domain.interactors.calendar.requests;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@Builder
public class GetEventsRequest {
    @NonNull
    Child child;
    @NonNull
    LocalDate date;
    @NonNull
    GetEventsFilter filter;
}
