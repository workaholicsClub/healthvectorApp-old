package ru.android.childdiary.domain.interactors.calendar.events.core;

import org.joda.time.DateTime;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter
@Builder(builderMethodName = "masterBuilder")
public class MasterEvent implements Serializable {
    Long masterEventId;

    EventType eventType;

    DateTime dateTime;

    Integer notifyTimeInMinutes;

    String note;

    Boolean isDone;

    Child child;

    RepeatParameters repeatParameters;

    Integer linearGroup;

    public MasterEvent getMasterEvent() {
        return this;
    }

    public MasterEvent.MasterEventBuilder toMasterBuilder() {
        return MasterEvent.masterBuilder()
                .masterEventId(masterEventId)
                .eventType(eventType)
                .dateTime(dateTime)
                .notifyTimeInMinutes(notifyTimeInMinutes)
                .note(note)
                .isDone(isDone)
                .child(child)
                .repeatParameters(repeatParameters)
                .linearGroup(linearGroup);
    }
}
