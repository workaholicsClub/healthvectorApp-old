package ru.android.childdiary.domain.interactors.development.testing.data.tests;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.core.Question;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.core.SimpleTest;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AutismTest extends SimpleTest {
    @NonNull
    String finishTextHigh;
    @NonNull
    String finishTextMedium;
    @NonNull
    String finishTextLow;
    @NonNull
    String shortTextHigh;
    @NonNull
    String shortTextMedium;
    @NonNull
    String shortTextLow;

    @Builder
    public AutismTest(@NonNull String name,
                      @NonNull String description,
                      @NonNull List<Question> questions,
                      @NonNull String finishTextHigh,
                      @NonNull String finishTextMedium,
                      @NonNull String finishTextLow,
                      @NonNull String shortTextHigh,
                      @NonNull String shortTextMedium,
                      @NonNull String shortTextLow) {
        super(TestType.AUTISM, name, description, questions);
        this.finishTextHigh = finishTextHigh;
        this.finishTextMedium = finishTextMedium;
        this.finishTextLow = finishTextLow;
        this.shortTextHigh = shortTextHigh;
        this.shortTextMedium = shortTextMedium;
        this.shortTextLow = shortTextLow;
    }
}
