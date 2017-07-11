package ru.android.childdiary.domain.interactors.development.testing.processors.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;

@ToString
@EqualsAndHashCode
@AllArgsConstructor(suppressConstructorProperties = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter
public abstract class BaseTestProcessor<T extends Test> implements TestProcessor {
    @NonNull
    T test;
}
