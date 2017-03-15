package ru.android.childdiary.domain.interactors.calendar;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class FoodMeasure {
    Long id;

    String name;
}
