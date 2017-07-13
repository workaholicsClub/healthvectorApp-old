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

import static ru.android.childdiary.data.types.DomanTestParameter.PHYSICAL_MANUAL;
import static ru.android.childdiary.data.types.DomanTestParameter.PHYSICAL_MOBILITY;
import static ru.android.childdiary.data.types.DomanTestParameter.PHYSICAL_SPEECH;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DomanPhysicalTest extends DomanTest {
    @Builder
    public DomanPhysicalTest(@NonNull String name,
                             @NonNull String description,
                             @NonNull Map<DomanTestParameter, List<Question>> questions,
                             @NonNull String resultTextFormat,
                             @NonNull String advanced, @NonNull String normal, @NonNull String slow,
                             @NonNull List<String> stageDescriptions) {
        super(TestType.DOMAN_PHYSICAL, name, description, questions, resultTextFormat, advanced, normal, slow, stageDescriptions);
    }

    @Override
    public DomanTestParameter[] getParameters() {
        return new DomanTestParameter[]{
                PHYSICAL_MOBILITY,
                PHYSICAL_SPEECH,
                PHYSICAL_MANUAL
        };
    }
}
