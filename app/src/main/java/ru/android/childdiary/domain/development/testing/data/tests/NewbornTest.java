package ru.android.childdiary.domain.development.testing.data.tests;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.development.testing.data.tests.core.Question;
import ru.android.childdiary.domain.development.testing.data.tests.core.SimpleTest;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NewbornTest extends SimpleTest {
    @Builder
    public NewbornTest(@NonNull String nameEn,
                       @NonNull String nameRu,
                       @NonNull String descriptionEn,
                       @NonNull String descriptionRu,
                       @NonNull List<Question> questions) {
        super(TestType.NEWBORN, nameEn, nameRu, descriptionEn, descriptionRu, questions);
    }
}
