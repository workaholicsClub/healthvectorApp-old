package ru.android.childdiary.domain.interactors.calendar.events.standard;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.DiaperState;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.core.ContentObject;
import ru.android.childdiary.domain.interactors.calendar.events.core.LinearGroupFieldType;
import ru.android.childdiary.domain.interactors.calendar.events.core.LinearGroupItem;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;

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
                        Integer notifyTimeInMinutes,
                        String note,
                        Boolean isDone,
                        Child child,
                        Integer linearGroup,
                        Long id,
                        DiaperState diaperState) {
        super(masterEventId, eventType, dateTime, notifyTimeInMinutes, note, isDone, child, linearGroup);
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
