package ru.android.childdiary.data.entities.calendar.events.core;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Table;

@Table(name = "repeat_parameters")
@Entity(name = "RepeatParametersEntity")
public interface RepeatParametersData {
    @Key
    @Generated
    Long getId();
}
