package ru.android.childdiary.domain.development.testing.data.tests.core;

import java.io.Serializable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Question implements Serializable {
    @NonNull
    String text;
}