package ru.android.childdiary.domain.interactors.calendar.events;

import org.joda.time.DateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.EventType;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter
@Builder(builderMethodName = "masterBuilder")
public class MasterEvent {
    Long masterEventId;

    EventType eventType;

    String description;

    DateTime dateTime;

    Integer notifyTimeInMinutes;

    String note;

    Boolean isDone;

    Boolean isDeleted;

    public MasterEvent getMasterEvent() {
        return this;
    }
}
