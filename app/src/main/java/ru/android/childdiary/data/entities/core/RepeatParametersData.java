package ru.android.childdiary.data.entities.core;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Table;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.core.LengthValue;

@Table(name = "repeat_parameters")
@Entity(name = "RepeatParametersEntity")
public interface RepeatParametersData {
    @Key
    @Generated
    Long getId();

    LinearGroups getFrequency();

    PeriodicityType getPeriodicity();

    LengthValue getLength();
}
