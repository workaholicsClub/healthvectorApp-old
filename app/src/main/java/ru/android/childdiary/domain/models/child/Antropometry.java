package ru.android.childdiary.domain.models.child;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Antropometry {
    long id;

    Child child;

    double height;

    double weight;

    LocalDate date;
}
