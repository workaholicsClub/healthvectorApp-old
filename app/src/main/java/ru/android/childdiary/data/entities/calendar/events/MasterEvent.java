package ru.android.childdiary.data.entities.calendar.events;

import org.joda.time.DateTime;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Table;
import ru.android.childdiary.data.types.EventType;

@Table(name = "master_event")
@Entity(name = "MasterEventEntity")
public interface MasterEvent {
    @Key
    @Generated
    Long getId();

    EventType getEventType();

    String getDescription();

    DateTime getDateTime();

    int getNotifyTimeInMinutes();

    String getNote();

    boolean isDone();

    boolean isDeleted();
}
