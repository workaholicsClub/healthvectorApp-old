package ru.android.childdiary.domain.interactors.calendar.events.standard;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SleepEvent extends MasterEvent {
    Long id;

    DateTime finishDateTime;

    Boolean isTimerStarted;

    @Builder(toBuilder = true)
    private SleepEvent(Long masterEventId,
                       EventType eventType,
                       DateTime dateTime,
                       Integer notifyTimeInMinutes,
                       String note,
                       Boolean isDone,
                       Child child,
                       RepeatParameters repeatParameters,
                       Integer linearGroup,
                       Long id,
                       DateTime finishDateTime,
                       Boolean isTimerStarted) {
        super(masterEventId, eventType, dateTime, notifyTimeInMinutes, note, isDone, child, repeatParameters, linearGroup);
        this.id = id;
        this.finishDateTime = finishDateTime;
        this.isTimerStarted = isTimerStarted;
    }
}
