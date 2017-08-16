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
public class AutismTest extends SimpleTest {
    @Builder
    public AutismTest(@NonNull String name,
                      @NonNull String description,
                      @NonNull List<Question> questions) {
        super(TestType.AUTISM, name, description, questions);
    }
}