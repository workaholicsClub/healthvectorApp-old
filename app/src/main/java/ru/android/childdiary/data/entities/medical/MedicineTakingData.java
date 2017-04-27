package ru.android.childdiary.data.entities.medical;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.ReferentialAction;
import io.requery.Table;
import ru.android.childdiary.data.entities.medical.core.MedicineData;

@Table(name = "medicine_taking")
@Entity(name = "MedicineTakingEntity")
public interface MedicineTakingData {
    @Key
    @Generated
    Long getId();

    @ForeignKey(delete = ReferentialAction.SET_NULL)
    @ManyToOne
    MedicineData getMedicineData();
}
