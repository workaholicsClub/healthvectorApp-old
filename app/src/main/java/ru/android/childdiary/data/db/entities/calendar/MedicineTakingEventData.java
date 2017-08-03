package ru.android.childdiary.data.db.entities.calendar;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.OneToOne;
import io.requery.Persistable;
import io.requery.ReferentialAction;
import io.requery.Table;
import ru.android.childdiary.data.db.entities.calendar.core.MasterEventData;
import ru.android.childdiary.data.db.entities.medical.MedicineTakingData;
import ru.android.childdiary.data.db.entities.dictionaries.MedicineData;
import ru.android.childdiary.data.db.entities.dictionaries.MedicineMeasureData;

@Table(name = "medicine_taking_event")
@Entity(name = "MedicineTakingEventEntity")
public interface MedicineTakingEventData extends Persistable {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @OneToOne
    MasterEventData getMasterEvent();

    @ForeignKey(delete = ReferentialAction.SET_NULL)
    @ManyToOne
    MedicineTakingData getMedicineTaking();

    @ForeignKey(delete = ReferentialAction.RESTRICT)
    @ManyToOne
    MedicineData getMedicine();

    Double getAmount();

    @ForeignKey(delete = ReferentialAction.RESTRICT)
    @ManyToOne
    MedicineMeasureData getMedicineMeasure();

    String getImageFileName();
}
