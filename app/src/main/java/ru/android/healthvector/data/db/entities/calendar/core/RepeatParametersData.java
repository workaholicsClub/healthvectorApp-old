package ru.android.healthvector.data.db.entities.calendar.core;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Table;
import ru.android.healthvector.domain.calendar.data.core.LengthValue;
import ru.android.healthvector.domain.calendar.data.core.LinearGroups;
import ru.android.healthvector.domain.calendar.data.core.PeriodicityType;

@Table(name = "repeat_parameters")
@Entity(name = "RepeatParametersEntity")
public interface RepeatParametersData extends Persistable {
    @Key
    @Generated
    Long getId();

    LinearGroups getFrequency();

    PeriodicityType getPeriodicity();

    LengthValue getLength();
}
