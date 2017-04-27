package ru.android.childdiary.data.entities.calendar.events;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.OneToOne;
import io.requery.Table;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.medical.MedicineTakingData;

@Table(name = "medicine_taking_event")
@Entity(name = "MedicineTakingEventEntity")
public interface MedicineTakingEventData {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @OneToOne
    MasterEventData getMasterEvent();

    @ForeignKey
    @ManyToOne
    MedicineTakingData getMedicineTakingData();

    // TODO: периодичность
}
