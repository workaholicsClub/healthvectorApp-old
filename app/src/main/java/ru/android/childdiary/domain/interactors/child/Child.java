package ru.android.childdiary.domain.interactors.child;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.types.Sex;

@Value
@Builder
public class Child {
    long id;

    String name;

    LocalDate birthDate;

    LocalTime birthTime;

    Sex sex;

    String imageFileName;

    double height;

    double weight;
}
