package ru.android.childdiary.domain.interactors.calendar.events.standard;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OtherEvent extends MasterEvent {
    Long id;

    String name;

    DateTime finishDateTime;

    @Builder(toBuilder = true)
    private OtherEvent(Long masterEventId,
                       EventType eventType,
                       DateTime dateTime,
                       Integer notifyTimeInMinutes,
                       String note,
                       Boolean isDone,
                       Boolean isDeleted,
                       Child child,
                       Long id,
                       String name,
                       DateTime finishDateTime) {
        super(masterEventId, eventType, dateTime, notifyTimeInMinutes, note, isDone, isDeleted, child);
        this.id = id;
        this.name = name;
        this.finishDateTime = finishDateTime;
    }
}
