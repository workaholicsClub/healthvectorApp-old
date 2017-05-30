package ru.android.childdiary.domain.interactors.development.antropometry;

import org.joda.time.LocalDate;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.child.Child;

@Value
@Builder(toBuilder = true)
public class Antropometry implements Serializable {
    Long id;

    Child child;

    Double height;

    Double weight;

    LocalDate date;
}
