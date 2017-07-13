package ru.android.childdiary.domain.interactors.development.testing.tests.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;

@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class DomanTest extends Test {
    public static final int MIN_WEEKS = 3;
    public static final int MAX_YEARS = 9;

    @NonNull
    Map<DomanTestParameter, List<Question>> questions;
    @NonNull
    String resultTextFormat;

    public DomanTest(@NonNull TestType testType,
                     @NonNull String name,
                     @NonNull String description,
                     @NonNull Map<DomanTestParameter, List<Question>> questions,
                     @NonNull String resultTextFormat) {
        super(testType, name, description);
        Map<DomanTestParameter, List<Question>> map = new HashMap<>();
        for (DomanTestParameter parameter : questions.keySet()) {
            List<Question> list = questions.get(parameter);
            map.put(parameter, Collections.unmodifiableList(list));
        }
        this.questions = Collections.unmodifiableMap(map);
        this.resultTextFormat = resultTextFormat;
    }
}
