package ru.android.childdiary.domain.interactors.calendar;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class FoodMeasure implements Serializable {
    public static final FoodMeasure NULL = FoodMeasure.builder().build();

    Long id;

    String name;
}
