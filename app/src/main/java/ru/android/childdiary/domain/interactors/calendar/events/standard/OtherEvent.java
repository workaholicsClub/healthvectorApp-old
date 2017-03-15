package ru.android.childdiary.domain.interactors.calendar.events.standard;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;

@Value
@EqualsAndHashCode(callSuper = true)
public class OtherEvent extends MasterEvent {
    Long id;

    DateTime finishDateTime;

    @Builder(toBuilder = true)
    private OtherEvent(Long masterEventId,
                       EventType eventType,
                       String description,
                       DateTime dateTime,
                       Integer notifyTimeInMinutes,
                       String note,
                       Boolean isDone,
                       Boolean isDeleted,
                       Long id,
                       DateTime finishDateTime) {
        super(masterEventId, eventType, description, dateTime, notifyTimeInMinutes, note, isDone, isDeleted);
        this.id = id;
        this.finishDateTime = finishDateTime;
    }
}
