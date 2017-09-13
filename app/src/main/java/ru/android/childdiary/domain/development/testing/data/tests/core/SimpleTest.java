package ru.android.childdiary.domain.development.testing.data.tests.core;

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
                      @NonNull String nameEn,
                      @NonNull String nameRu,
                      @NonNull String descriptionEn,
                      @NonNull String descriptionRu,
                      @NonNull List<Question> questions) {
        super(testType, nameEn, nameRu, descriptionEn, descriptionRu);
        this.questions = Collections.unmodifiableList(questions);
    }
}
