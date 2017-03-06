package ru.android.childdiary.domain.interactors.calendar.events;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.types.EventType;

@Value
@Builder
public class MasterEvent {
    Long id;

    EventType eventType;

    String description;

    DateTime dateTime;

    Integer notifyTimeInMinutes;

    String note;

    Boolean isDone;

    Boolean isDeleted;
}
