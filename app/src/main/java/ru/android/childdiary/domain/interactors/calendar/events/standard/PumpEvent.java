package ru.android.childdiary.domain.interactors.calendar.events.standard;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.android.childdiary.data.types.Breast;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.events.MasterEvent;

@Value
@EqualsAndHashCode(callSuper = true)
public class PumpEvent extends MasterEvent {
    Long id;

    Breast breast;

    Double leftAmountMl;

    Double rightAmountMl;

    @Builder(toBuilder = true)
    private PumpEvent(Long masterEventId,
                      EventType eventType,
                      String description,
                      DateTime dateTime,
                      Integer notifyTimeInMinutes,
                      String note,
                      Boolean isDone,
                      Boolean isDeleted,
                      Long id,
                      Breast breast,
                      Double leftAmountMl,
                      Double rightAmountMl) {
        super(masterEventId, eventType, description, dateTime, notifyTimeInMinutes, note, isDone, isDeleted);
        this.id = id;
        this.breast = breast;
        this.leftAmountMl = leftAmountMl;
        this.rightAmountMl = rightAmountMl;
    }
}
