package ru.android.childdiary.data.entities.calendar.events.core;

import org.joda.time.DateTime;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;
import io.requery.Table;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.types.EventType;

@Table(name = "master_event")
@Entity(name = "MasterEventEntity")
public interface MasterEventData extends Persistable {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @ManyToOne
    ChildData getChild();

    EventType getEventType();

    DateTime getDateTime();

    Integer getNotifyTimeInMinutes();

    String getNote();

    Boolean isDone();

    Integer getLinearGroup();
}
