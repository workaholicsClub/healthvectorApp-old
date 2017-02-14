package ru.android.childdiary.data.entities.child;

import org.joda.time.LocalDate;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Table;

@Table(name = "antropometry")
@Entity(name = "AntropometryEntity")
public interface AntropometryData {
    @Key
    @Generated
    long getId();

    @ForeignKey
    @ManyToOne
    ChildData getChild();

    double getHeight();

    double getWeight();

    LocalDate getDate();
}
