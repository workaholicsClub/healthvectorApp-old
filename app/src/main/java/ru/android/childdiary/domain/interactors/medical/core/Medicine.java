package ru.android.childdiary.domain.interactors.medical.core;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Medicine implements Serializable {
    public static final Medicine NULL = Medicine.builder().build();

    Long id;

    String name;
}
