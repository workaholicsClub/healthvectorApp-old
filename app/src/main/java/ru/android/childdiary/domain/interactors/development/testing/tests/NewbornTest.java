package ru.android.childdiary.domain.interactors.development.testing.tests;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Question;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.SimpleTest;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NewbornTest extends SimpleTest {
    @NonNull
    String resultBad;
    @NonNull
    String resultGood;
    @NonNull
    String shortResultBad;
    @NonNull
    String shortResultGood;

    @Builder
    public NewbornTest(@NonNull String name,
                       @NonNull String description,
                       @NonNull List<Question> questions,
                       @NonNull String resultBad,
                       @NonNull String resultGood,
                       @NonNull String shortResultBad,
                       @NonNull String shortResultGood) {
        super(TestType.NEWBORN, name, description, questions);
        this.resultBad = resultBad;
        this.resultGood = resultGood;
        this.shortResultBad = shortResultBad;
        this.shortResultGood = shortResultGood;
    }
}
