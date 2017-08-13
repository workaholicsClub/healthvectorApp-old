package ru.android.childdiary.domain.development.testing.data.tests;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.development.testing.data.tests.core.DomanTest;
import ru.android.childdiary.domain.development.testing.data.tests.core.Question;

import static ru.android.childdiary.data.types.DomanTestParameter.PHYSICAL_MANUAL;
import static ru.android.childdiary.data.types.DomanTestParameter.PHYSICAL_MOBILITY;
import static ru.android.childdiary.data.types.DomanTestParameter.PHYSICAL_SPEECH;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DomanPhysicalTest extends DomanTest {
    public static final DomanTestParameter[] PARAMETERS = new DomanTestParameter[]{
            PHYSICAL_MOBILITY,
            PHYSICAL_SPEECH,
            PHYSICAL_MANUAL
    };

    @Builder
    public DomanPhysicalTest(@NonNull String name,
                             @NonNull String description,
                             @NonNull Map<DomanTestParameter, List<Question>> questions) {
        super(TestType.DOMAN_PHYSICAL, name, description, questions);
    }

    @Override
    public DomanTestParameter[] getParameters() {
        return PARAMETERS;
    }
}
