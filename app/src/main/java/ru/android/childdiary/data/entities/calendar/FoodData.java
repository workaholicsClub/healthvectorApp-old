package ru.android.childdiary.data.entities.calendar;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Table;

// TODO: translation table
@Table(name = "food")
@Entity(name = "FoodEntity")
public interface FoodData {
    @Key
    @Generated
    Long getId();

    String getName();
}
