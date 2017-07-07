package ru.android.childdiary.domain.interactors.development.testing;

import java.io.Serializable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.data.types.TestType;

@Value
@Builder
public class Test implements Serializable {
    @NonNull
    TestType testType;
    @NonNull
    String name;
}
