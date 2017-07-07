package ru.android.childdiary.domain.interactors.development.testing;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.types.TestType;

@Value
@Builder(toBuilder = true)
public class Test implements Serializable {
    TestType testType;

    String name;
}
