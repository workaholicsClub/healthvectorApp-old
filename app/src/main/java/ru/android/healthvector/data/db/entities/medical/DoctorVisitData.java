package ru.android.healthvector.data.db.entities.medical;

import org.joda.time.DateTime;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.OneToOne;
import io.requery.Persistable;
import io.requery.ReferentialAction;
import io.requery.Table;
import ru.android.healthvector.data.db.entities.child.ChildData;
import ru.android.healthvector.data.db.entities.calendar.core.RepeatParametersData;
import ru.android.healthvector.data.db.entities.dictionaries.DoctorData;

@Table(name = "doctor_visit")
@Entity(name = "DoctorVisitEntity")
public interface DoctorVisitData extends Persistable {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @ManyToOne
    ChildData getChild();

    @ForeignKey(delete = ReferentialAction.RESTRICT)
    @ManyToOne
    DoctorData getDoctor();

    @ForeignKey(delete = ReferentialAction.RESTRICT)
    @OneToOne
    RepeatParametersData getRepeatParameters();

    String getName();

    Integer getDurationInMinutes();

    DateTime getDateTime();

    DateTime getFinishDateTime();

    Boolean isExported();

    Integer getNotifyTimeInMinutes();

    String getNote();

    String getImageFileName();

    Boolean isDeleted();
}
