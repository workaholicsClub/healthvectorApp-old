package ru.android.childdiary.domain.development.testing.data.tests.core;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.TestType;

@ToString
@EqualsAndHashCode
@AllArgsConstructor(suppressConstructorProperties = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter
public abstract class Test implements Serializable {
    @NonNull
    TestType testType;
    @NonNull
    String name;
    @NonNull
    String description;
}
