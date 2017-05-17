package ru.android.childdiary.data.entities.core;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Table;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;

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
