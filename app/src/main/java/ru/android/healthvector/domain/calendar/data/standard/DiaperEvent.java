package ru.android.healthvector.domain.calendar.data.standard;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.healthvector.data.types.DiaperState;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.domain.calendar.data.core.LinearGroupFieldType;
import ru.android.healthvector.domain.calendar.data.core.LinearGroupItem;
import ru.android.healthvector.domain.calendar.data.core.MasterEvent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.core.data.ContentObject;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DiaperEvent extends MasterEvent implements ContentObject<DiaperEvent>, LinearGroupItem<DiaperEvent> {
    private static final DiaperEvent NULL = DiaperEvent.builder().build();

    Long id;

    DiaperState diaperState;

    @Builder(toBuilder = true)
    private DiaperEvent(Long masterEventId,
                        EventType eventType,
                        DateTime dateTime,
                        DateTime notifyDateTime,
                        Integer notifyTimeInMinutes,
                        String note,
                        Boolean isDone,
                        Child child,
                        Integer linearGroup,
                        Long id,
                        DiaperState diaperState) {
        super(masterEventId, eventType, dateTime, notifyDateTime, notifyTimeInMinutes, note, isDone, child, linearGroup);
        this.id = id;
        this.diaperState = diaperState;
    }

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull DiaperEvent other) {
        return contentEquals(this, other)
                && getDiaperState() == other.getDiaperState();
    }

    @Override
    public List<LinearGroupFieldType> getChangedFields(DiaperEvent other) {
        return Collections.emptyList();
    }
}
