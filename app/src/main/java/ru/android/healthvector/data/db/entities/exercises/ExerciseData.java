package ru.android.healthvector.data.db.entities.exercises;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Table;

@Table(name = "exercise")
@Entity(name = "ExerciseEntity")
public interface ExerciseData extends Persistable {
    @Key
    @Generated
    Long getId();

    Long getServerId();

    String getCode();

    String getNameEn();

    String getNameRu();

    String getDescriptionEn();

    String getDescriptionRu();

    Integer getOrderNumber();
}
