package ru.android.childdiary.domain.interactors.child;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.types.Sex;

@Value
@Builder(toBuilder = true)
public class Child implements Serializable {
    public static final Child NULL = Child.builder().build();

    Long id;

    String name;

    LocalDate birthDate;

    // необязательный параметр
    LocalTime birthTime;

    Sex sex;

    // необязательный параметр
    String imageFileName;

    Double birthHeight;

    Double birthWeight;
}
