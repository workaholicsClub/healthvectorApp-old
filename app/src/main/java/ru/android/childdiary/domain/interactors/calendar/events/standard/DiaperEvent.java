package ru.android.childdiary.domain.interactors.calendar.events.standard;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.DiaperState;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.calendar.events.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DiaperEvent extends MasterEvent {
    Long id;

    DiaperState diaperState;

    @Builder(toBuilder = true)
    private DiaperEvent(Long masterEventId,
                        EventType eventType,
                        DateTime dateTime,
                        Integer notifyTimeInMinutes,
                        String note,
                        Boolean isDone,
                        Child child,
                        RepeatParameters repeatParameters,
                        Integer linearGroup,
                        Long id,
                        DiaperState diaperState) {
        super(masterEventId, eventType, dateTime, notifyTimeInMinutes, note, isDone, child, repeatParameters, linearGroup);
        this.id = id;
        this.diaperState = diaperState;
    }
}
