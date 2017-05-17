package ru.android.childdiary.data.entities.medical.core;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Table;

// TODO: translation table
@Table(name = "doctor")
@Entity(name = "DoctorEntity")
public interface DoctorData extends Persistable {
    @Key
    @Generated
    Long getId();

    String getName();
}
