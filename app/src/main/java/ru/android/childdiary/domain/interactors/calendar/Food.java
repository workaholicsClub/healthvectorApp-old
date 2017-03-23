package ru.android.childdiary.domain.interactors.calendar;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Food implements Serializable {
    public static final Food NULL = Food.builder().build();

    Long id;

    String name;
}
