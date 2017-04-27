package ru.android.childdiary.data.entities.medical.core;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Table;

// TODO: translation table
@Table(name = "doctor")
@Entity(name = "DoctorEntity")
public interface DoctorData {
    @Key
    @Generated
    Long getId();

    String getName();
}
