package ru.android.healthvector.domain.development.testing.data.processors.core;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;

@ToString
@EqualsAndHashCode
@AllArgsConstructor(suppressConstructorProperties = true)
public abstract class BaseTestProcessor<T extends Test> implements BiTestProcessor {
    @Getter
    @NonNull
    protected final T test;
}
