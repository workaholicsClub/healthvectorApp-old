package ru.android.healthvector.domain.calendar.data.core;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.utils.ObjectUtils;

@ToString
@EqualsAndHashCode
@AllArgsConstructor(suppressConstructorProperties = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter
@Builder(builderMethodName = "masterBuilder")
public class MasterEvent implements Serializable {
    public static final MasterEvent NULL = MasterEvent.masterBuilder().build();

    Long masterEventId;

    EventType eventType;

    DateTime dateTime;

    DateTime notifyDateTime;

    Integer notifyTimeInMinutes;

    String note;

    Boolean isDone;

    Child child;

    Integer linearGroup;

    protected static boolean contentEquals(@NonNull MasterEvent event1, @NonNull MasterEvent event2) {
        return ObjectUtils.equalsToMinutes(event1.getDateTime(), event2.getDateTime())
                && ObjectUtils.equals(event1.getNotifyTimeInMinutes(), event2.getNotifyTimeInMinutes())
                && ObjectUtils.contentEquals(event1.getNote(), event2.getNote());
    }

    public static List<LinearGroupFieldType> getChangedFields(@NonNull MasterEvent event1, @NonNull MasterEvent event2) {
        List<LinearGroupFieldType> significantFields = new ArrayList<>();

        if (!ObjectUtils.equals(event1.getNotifyTimeInMinutes(), event2.getNotifyTimeInMinutes())) {
            significantFields.add(LinearGroupFieldType.NOTIFY_TIME_IN_MINUTES);
        }

        return significantFields;
    }

    public MasterEvent.MasterEventBuilder toMasterBuilder() {
        return MasterEvent.masterBuilder()
                .masterEventId(masterEventId)
                .eventType(eventType)
                .dateTime(dateTime)
                .notifyDateTime(notifyDateTime)
                .notifyTimeInMinutes(notifyTimeInMinutes)
                .note(note)
                .isDone(isDone)
                .child(child)
                .linearGroup(linearGroup);
    }
}
