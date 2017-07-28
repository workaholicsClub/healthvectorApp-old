package ru.android.childdiary.domain.interactors.calendar.requests;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.data.types.EventType;

@Value
public class GetEventsFilter {
    @NonNull
    Set<EventType> eventTypes;

    @Builder
    public GetEventsFilter(@NonNull Collection<EventType> eventTypes) {
        this.eventTypes = Collections.unmodifiableSet(new HashSet<>(eventTypes));
    }
}
