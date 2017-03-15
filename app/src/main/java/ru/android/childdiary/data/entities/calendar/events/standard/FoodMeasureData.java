package ru.android.childdiary.data.entities.calendar.events.standard;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Table;

// TODO: translation table
@Table(name = "food_measure")
@Entity(name = "FoodMeasureEntity")
public interface FoodMeasureData {
    @Key
    @Generated
    Long getId();

    String getName();
}
