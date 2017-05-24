package ru.android.childdiary.domain.interactors.calendar.events.standard;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.core.ContentObject;
import ru.android.childdiary.domain.interactors.calendar.events.core.LinearGroupFieldType;
import ru.android.childdiary.domain.interactors.calendar.events.core.LinearGroupItem;
import ru.android.childdiary.domain.interactors.calendar.events.core.MasterEvent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SleepEvent extends MasterEvent implements ContentObject<SleepEvent>, LinearGroupItem<SleepEvent> {
    private static final SleepEvent NULL = SleepEvent.builder().build();

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
                       Integer linearGroup,
                       Long id,
                       DateTime finishDateTime,
                       Boolean isTimerStarted) {
        super(masterEventId, eventType, dateTime, notifyTimeInMinutes, note, isDone, child, linearGroup);
        this.id = id;
        this.finishDateTime = finishDateTime;
        this.isTimerStarted = isTimerStarted;
    }

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull SleepEvent other) {
        return contentEquals(getMasterEvent(), other.getMasterEvent())
                && ObjectUtils.equalsToMinutes(getFinishDateTime(), other.getFinishDateTime());
    }

    @Override
    public List<LinearGroupFieldType> getChangedFields(SleepEvent other) {
        return Collections.emptyList();
    }
}
