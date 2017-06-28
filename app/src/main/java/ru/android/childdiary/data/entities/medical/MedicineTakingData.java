package ru.android.childdiary.data.entities.medical;

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
import ru.android.childdiary.data.entities.child.ChildData;
import ru.android.childdiary.data.entities.core.RepeatParametersData;
import ru.android.childdiary.data.entities.medical.core.MedicineData;
import ru.android.childdiary.data.entities.medical.core.MedicineMeasureData;

@Table(name = "medicine_taking")
@Entity(name = "MedicineTakingEntity")
public interface MedicineTakingData extends Persistable {
    @Key
    @Generated
    Long getId();

    @ForeignKey
    @ManyToOne
    ChildData getChild();

    @ForeignKey(delete = ReferentialAction.RESTRICT)
    @ManyToOne
    MedicineData getMedicine();

    Double getAmount();

    @ForeignKey(delete = ReferentialAction.RESTRICT)
    @ManyToOne
    MedicineMeasureData getMedicineMeasure();

    @ForeignKey(delete = ReferentialAction.RESTRICT)
    @OneToOne
    RepeatParametersData getRepeatParameters();

    DateTime getDateTime();

    DateTime getFinishDateTime();

    Boolean isExported();

    Integer getNotifyTimeInMinutes();

    String getNote();

    String getImageFileName();

    Boolean isDeleted();
}
