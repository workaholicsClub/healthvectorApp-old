package ru.android.childdiary.domain.interactors.development.testing.data.tests.core;

import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.TestType;

@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SimpleTest extends Test {
    @NonNull
    List<Question> questions;

    public SimpleTest(@NonNull TestType testType,
                      @NonNull String name,
                      @NonNull String description,
                      @NonNull List<Question> questions) {
        super(testType, name, description);
        this.questions = Collections.unmodifiableList(questions);
    }
}
