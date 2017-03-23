package ru.android.childdiary.domain.interactors.child;

import org.joda.time.LocalDate;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Antropometry implements Serializable {
    public static final Antropometry NULL = Antropometry.builder().build();

    Long id;

    Double height;

    Double weight;

    LocalDate date;
}
