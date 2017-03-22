package ru.android.childdiary.domain.interactors.calendar.events.standard;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@EqualsAndHashCode(callSuper = true)
public class SleepEvent extends MasterEvent {
    Long id;

    DateTime finishDateTime;

    Boolean isTimerStarted;

    @Builder(toBuilder = true)
    private SleepEvent(Long masterEventId,
                       EventType eventType,
                       String description,
                       DateTime dateTime,
                       Integer notifyTimeInMinutes,
                       String note,
                       Boolean isDone,
                       Boolean isDeleted,
                       Child child,
                       Long id,
                       DateTime finishDateTime,
                       Boolean isTimerStarted) {
        super(masterEventId, eventType, description, dateTime, notifyTimeInMinutes, note, isDone, isDeleted, child);
        this.id = id;
        this.finishDateTime = finishDateTime;
        this.isTimerStarted = isTimerStarted;
    }
}
