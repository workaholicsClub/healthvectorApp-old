package ru.android.childdiary.domain.interactors.development.testing.processors.core;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;

@ToString
@EqualsAndHashCode
@AllArgsConstructor(suppressConstructorProperties = true)
public abstract class BaseTestProcessor<T extends Test> implements TestProcessor {
    @NonNull
    private final T test;
}
