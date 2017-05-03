package ru.android.childdiary.data.entities.medical.core;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Table;

// TODO: translation table
@Table(name = "medicine_measure")
@Entity(name = "MedicineMeasureEntity")
public interface MedicineMeasureData {
    @Key
    @Generated
    Long getId();

    String getName();
}
