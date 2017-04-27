package ru.android.childdiary.data.entities.calendar.events.standard;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.OneToOne;
import io.requery.Table;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.types.DiaperState;

@Table(name = "diaper_event")
@Entity(name = "DiaperEventEntity")
public interface DiaperEventData {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @OneToOne
    MasterEventData getMasterEvent();

    DiaperState getDiaperState();
}
