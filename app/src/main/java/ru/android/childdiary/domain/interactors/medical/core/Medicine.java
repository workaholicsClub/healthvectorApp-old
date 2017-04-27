package ru.android.childdiary.domain.interactors.medical.core;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Medicine {
    public static final Medicine NULL = Medicine.builder().build();

    Long id;

    String name;
}
