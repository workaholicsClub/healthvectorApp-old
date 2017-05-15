package ru.android.childdiary.domain.interactors.medical.core;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Doctor implements Serializable {
    public static final Doctor NULL = Doctor.builder().build();

    Long id;

    String name;
}
