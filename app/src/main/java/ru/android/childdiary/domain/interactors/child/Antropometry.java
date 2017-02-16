package ru.android.childdiary.domain.interactors.child;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Antropometry {
    long id;

    @NonNull
    Child child;

    double height;

    double weight;

    LocalDate date;
}
