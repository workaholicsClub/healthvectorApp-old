package ru.android.childdiary.data.entities.calendar.events.core;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;
import io.requery.Table;

// TODO: translation table
@Table(name = "food")
@Entity(name = "FoodEntity")
public interface FoodData extends Persistable {
    @Key
    @Generated
    Long getId();

    String getName();
}
