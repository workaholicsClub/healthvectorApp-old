package ru.android.childdiary.data.entities.calendar.events.standard;

import org.joda.time.DateTime;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.OneToOne;
import io.requery.Table;
import ru.android.childdiary.data.entities.calendar.events.MasterEventData;

@Table(name = "other_event")
@Entity(name = "OtherEventEntity")
public interface OtherEventData {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @OneToOne
    MasterEventData getMasterEvent();

    DateTime getFinishDateTime();
}
