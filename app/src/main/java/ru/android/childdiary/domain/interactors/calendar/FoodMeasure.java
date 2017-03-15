package ru.android.childdiary.domain.interactors.calendar;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class FoodMeasure {
    public static final FoodMeasure NULL = FoodMeasure.builder().build();

    Long id;

    String name;
}
