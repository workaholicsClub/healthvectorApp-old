package ru.android.healthvector.data.db.entities.child;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Table;
import ru.android.healthvector.data.types.Sex;

@Table(name = "child")
@Entity(name = "ChildEntity")
public interface ChildData extends Persistable {
    @Key
    @Generated
    Long getId();

    String getName();

    LocalDate getBirthDate();

    // необязательный параметр
    LocalTime getBirthTime();

    Sex getSex();

    // необязательный параметр
    String getImageFileName();

    Double getBirthHeight();

    Double getBirthWeight();
}
