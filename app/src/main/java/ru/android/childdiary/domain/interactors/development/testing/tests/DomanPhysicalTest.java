package ru.android.childdiary.domain.interactors.development.testing.tests;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.DomanTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Question;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DomanPhysicalTest extends DomanTest {
    @Builder
    public DomanPhysicalTest(@NonNull String name,
                             @NonNull String description,
                             @NonNull Map<DomanTestParameter, List<Question>> questions,
                             @NonNull String resultTextFormat) {
        super(TestType.DOMAN_PHYSICAL, name, description, questions, resultTextFormat);
    }
}
