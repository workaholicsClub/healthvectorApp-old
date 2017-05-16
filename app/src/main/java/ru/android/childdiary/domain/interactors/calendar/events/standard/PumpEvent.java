package ru.android.childdiary.domain.interactors.calendar.events.standard;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PumpEvent extends MasterEvent {
    Long id;

    Breast breast;

    Double leftAmountMl;

    Double rightAmountMl;

    @Builder(toBuilder = true)
    private PumpEvent(Long masterEventId,
                      EventType eventType,
                      DateTime dateTime,
                      Integer notifyTimeInMinutes,
                      String note,
                      Boolean isDone,
                      Child child,
                      Integer linearGroup,
                      Long id,
                      Breast breast,
                      Double leftAmountMl,
                      Double rightAmountMl) {
        super(masterEventId, eventType, dateTime, notifyTimeInMinutes, note, isDone, child, linearGroup);
        this.id = id;
        this.breast = breast;
        this.leftAmountMl = leftAmountMl;
        this.rightAmountMl = rightAmountMl;
    }
}
