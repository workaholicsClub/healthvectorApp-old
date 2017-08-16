package ru.android.childdiary.domain.calendar.data.standard;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.calendar.data.core.LinearGroupFieldType;
import ru.android.childdiary.domain.calendar.data.core.LinearGroupItem;
import ru.android.childdiary.domain.calendar.data.core.MasterEvent;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.core.data.ContentObject;
import ru.android.childdiary.utils.ObjectUtils;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OtherEvent extends MasterEvent implements ContentObject<OtherEvent>, LinearGroupItem<OtherEvent> {
    private static final OtherEvent NULL = OtherEvent.builder().build();

    Long id;

    String name;

    DateTime finishDateTime;

    @Builder(toBuilder = true)
    private OtherEvent(Long masterEventId,
                       EventType eventType,
                       DateTime dateTime,
                       DateTime notifyDateTime,
                       Integer notifyTimeInMinutes,
                       String note,
                       Boolean isDone,
                       Child child,
                       Integer linearGroup,
                       Long id,
                       String name,
                       DateTime finishDateTime) {
        super(masterEventId, eventType, dateTime, notifyDateTime, notifyTimeInMinutes, note, isDone, child, linearGroup);
        this.id = id;
        this.name = name;
        this.finishDateTime = finishDateTime;
    }

    @Override
    public boolean isContentEmpty() {
        return isContentEqual(NULL);
    }

    @Override
    public boolean isContentEqual(@NonNull OtherEvent other) {
        return contentEquals(this, other)
                && ObjectUtils.contentEquals(getName(), other.getName())
                && ObjectUtils.equalsToMinutes(getFinishDateTime(), other.getFinishDateTime());
    }

    @Override
    public List<LinearGroupFieldType> getChangedFields(OtherEvent other) {
        return Collections.emptyList();
    }
}
