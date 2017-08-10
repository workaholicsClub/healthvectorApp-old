package ru.android.childdiary.domain.interactors.development.testing.data.tests;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.core.DomanTest;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.core.Question;

import static ru.android.childdiary.data.types.DomanTestParameter.MENTAL_AUDITION;
import static ru.android.childdiary.data.types.DomanTestParameter.MENTAL_SENSITIVITY;
import static ru.android.childdiary.data.types.DomanTestParameter.MENTAL_VISION;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DomanMentalTest extends DomanTest {
    public static final DomanTestParameter[] PARAMETERS = new DomanTestParameter[]{
            MENTAL_VISION,
            MENTAL_AUDITION,
            MENTAL_SENSITIVITY
    };

    @Builder
    public DomanMentalTest(@NonNull String name,
                           @NonNull String description,
                           @NonNull Map<DomanTestParameter, List<Question>> questions) {
        super(TestType.DOMAN_MENTAL, name, description, questions);
    }

    @Override
    public DomanTestParameter[] getParameters() {
        return PARAMETERS;
    }
}
