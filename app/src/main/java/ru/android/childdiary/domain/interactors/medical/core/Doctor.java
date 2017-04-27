package ru.android.childdiary.domain.interactors.medical.core;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Doctor {
    public static final Doctor NULL = Doctor.builder().build();

    Long id;

    String name;
}
