package ru.android.childdiary.data.entities.calendar.events.standard;

import org.joda.time.DateTime;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.OneToOne;
import io.requery.Persistable;
import io.requery.Table;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;

@Table(name = "sleep_event")
@Entity(name = "SleepEventEntity")
public interface SleepEventData extends Persistable {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @OneToOne
    MasterEventData getMasterEvent();

    DateTime getFinishDateTime();

    Boolean isTimerStarted();
}
