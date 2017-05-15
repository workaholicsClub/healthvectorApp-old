package ru.android.childdiary.domain.interactors.medical.core;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class MedicineMeasure implements Serializable {
    public static final MedicineMeasure NULL = MedicineMeasure.builder().build();

    Long id;

    String name;
}
