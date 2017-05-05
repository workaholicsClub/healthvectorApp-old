package ru.android.childdiary.data.entities.medical;

import org.joda.time.DateTime;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.OneToOne;
import io.requery.ReferentialAction;
import io.requery.Table;
import ru.android.childdiary.data.entities.calendar.events.core.RepeatParametersData;
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.medical.core.MedicineData;
import ru.android.childdiary.data.entities.medical.core.MedicineMeasureData;

@Table(name = "medicine_taking")
@Entity(name = "MedicineTakingEntity")
public interface MedicineTakingData {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @ManyToOne
    ChildData getChild();

    @ForeignKey(delete = ReferentialAction.SET_NULL)
    @ManyToOne
    MedicineData getMedicine();

    Double getAmount();

    @ForeignKey(delete = ReferentialAction.SET_NULL)
    @ManyToOne
    MedicineMeasureData getMedicineMeasure();

    @ForeignKey(delete = ReferentialAction.SET_NULL)
    @OneToOne
    RepeatParametersData getRepeatParameters();

    DateTime getDateTime();

    DateTime getFinishDateTime();

    Boolean getExported();

    Integer getNotifyTimeInMinutes();

    String getNote();

    String getImageFileName();
}
