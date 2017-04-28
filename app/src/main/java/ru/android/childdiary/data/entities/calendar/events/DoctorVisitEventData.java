package ru.android.childdiary.data.entities.calendar.events;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.OneToOne;
import io.requery.ReferentialAction;
import io.requery.Table;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventData;
import ru.android.childdiary.data.entities.medical.DoctorVisitData;

@Table(name = "doctor_visit_event")
@Entity(name = "DoctorVisitEventEntity")
public interface DoctorVisitEventData {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @OneToOne
    MasterEventData getMasterEvent();

    @ForeignKey(delete = ReferentialAction.SET_NULL)
    @ManyToOne
    DoctorVisitData getDoctorVisit();

    // TODO: периодичность
}
