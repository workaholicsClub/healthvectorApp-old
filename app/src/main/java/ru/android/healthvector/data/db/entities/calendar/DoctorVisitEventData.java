package ru.android.healthvector.data.db.entities.calendar;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.OneToOne;
import io.requery.Persistable;
import io.requery.ReferentialAction;
import io.requery.Table;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventData;
import ru.android.healthvector.data.db.entities.medical.DoctorVisitData;
import ru.android.healthvector.data.db.entities.dictionaries.DoctorData;

@Table(name = "doctor_visit_event")
@Entity(name = "DoctorVisitEventEntity")
public interface DoctorVisitEventData extends Persistable {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @OneToOne
    MasterEventData getMasterEvent();

    @ForeignKey(delete = ReferentialAction.SET_NULL)
    @ManyToOne
    DoctorVisitData getDoctorVisit();

    @ForeignKey(delete = ReferentialAction.RESTRICT)
    @ManyToOne
    DoctorData getDoctor();

    String getName();

    Integer getDurationInMinutes();

    String getImageFileName();
}
