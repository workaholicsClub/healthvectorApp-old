package ru.android.childdiary.data.entities.medical;

import org.joda.time.DateTime;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.ReferentialAction;
import io.requery.Table;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.medical.core.DoctorData;

@Table(name = "doctor_visit")
@Entity(name = "DoctorVisitEntity")
public interface DoctorVisitData {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @ManyToOne
    ChildData getChild();

    @ForeignKey(delete = ReferentialAction.SET_NULL)
    @ManyToOne
    DoctorData getDoctor();

    String getName();

    Integer getDurationInMinutes();

    DateTime getDateTime();

    Integer getNotifyTimeInMinutes();

    String getNote();

    String getImageFileName();
}
