package ru.android.healthvector.data.db.entities.calendar.standard;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.OneToOne;
import io.requery.Persistable;
import io.requery.Table;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventData;
import ru.android.healthvector.data.types.DiaperState;

@Table(name = "diaper_event")
@Entity(name = "DiaperEventEntity")
public interface DiaperEventData extends Persistable {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @OneToOne
    MasterEventData getMasterEvent();

    DiaperState getDiaperState();
}
