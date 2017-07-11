package ru.android.childdiary.data.db.entities.calendar.events.core;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Table;

// TODO: translation table
@Table(name = "food_measure")
@Entity(name = "FoodMeasureEntity")
public interface FoodMeasureData extends Persistable {
    @Key
    @Generated
    Long getId();

    String getName();
}