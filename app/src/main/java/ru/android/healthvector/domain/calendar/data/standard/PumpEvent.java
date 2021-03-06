package ru.android.healthvector.domain.calendar.data.standard;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.healthvector.data.types.Breast;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.domain.calendar.data.core.LinearGroupFieldType;
import ru.android.healthvector.domain.calendar.data.core.LinearGroupItem;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.core.data.ContentObject;
import ru.android.healthvector.utils.ObjectUtils;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PumpEvent extends MasterEvent implements ContentObject<PumpEvent>, LinearGroupItem<PumpEvent> {
    private static final PumpEvent NULL = PumpEvent.builder().build();

    Long id;

    Breast breast;

    Double leftAmountMl;

    Double rightAmountMl;

    @Builder(toBuilder = true)
    private PumpEvent(Long masterEventId,
                      EventType eventType,
                      DateTime dateTime,
                      DateTime notifyDateTime,
                      Integer notifyTimeInMinutes,
                      String note,
                      Boolean isDone,
                      Child child,
                      Integer linearGroup,
                      Long id,
                      Breast breast,
                      Double leftAmountMl,
                      Double rightAmountMl) {
        super(masterEventId, eventType, dateTime, notifyDateTime, notifyTimeInMinutes, note, isDone, child, linearGroup);
        this.id = id;
        this.breast = breast;
        this.leftAmountMl = leftAmountMl;
        this.rightAmountMl = rightAmountMl;
    }

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull PumpEvent other) {
        return contentEquals(this, other)
                && getBreast() == other.getBreast()
                && ObjectUtils.equals(getLeftAmountMl(), other.getLeftAmountMl())
                && ObjectUtils.equals(getRightAmountMl(), other.getRightAmountMl());
    }

    @Override
    public List<LinearGroupFieldType> getChangedFields(PumpEvent other) {
        return Collections.emptyList();
    }
}
