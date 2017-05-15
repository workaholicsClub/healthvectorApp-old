package ru.android.childdiary.data.entities.medical.core;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Table;

// TODO: translation table
@Table(name = "medicine")
@Entity(name = "MedicineEntity")
public interface MedicineData {
    @Key
    @Generated
    Long getId();

    String getName();
}
