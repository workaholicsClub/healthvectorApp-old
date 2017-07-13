package ru.android.childdiary.domain.interactors.development.testing.tests.core;

import android.support.annotation.Nullable;

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
    @NonNull
    String advanced, normal, slow;
    @NonNull
    List<String> stageDescriptions;

    public DomanTest(@NonNull TestType testType,
                     @NonNull String name,
                     @NonNull String description,
                     @NonNull Map<DomanTestParameter, List<Question>> questions,
                     @NonNull String resultTextFormat,
                     @NonNull String advanced, @NonNull String normal, @NonNull String slow,
                     @NonNull List<String> stageDescriptions) {
        super(testType, name, description);
        Map<DomanTestParameter, List<Question>> map = new HashMap<>();
        for (DomanTestParameter parameter : questions.keySet()) {
            List<Question> list = questions.get(parameter);
            map.put(parameter, Collections.unmodifiableList(list));
        }
        this.questions = Collections.unmodifiableMap(map);
        this.resultTextFormat = resultTextFormat;
        this.advanced = advanced;
        this.normal = normal;
        this.slow = slow;
        this.stageDescriptions = Collections.unmodifiableList(stageDescriptions);
    }

    public String getStageType(int initialStage, int stage) {
        if (initialStage == stage) {
            return normal;
        } else if (initialStage < stage) {
            return advanced;
        }
        return slow;
    }

    @Nullable
    public String getStageDescription(int stage) {
        int index = stage - 1;
        if (index >= 0 && index < stageDescriptions.size()) {
            return stageDescriptions.get(index);
        }
        return null;
    }

    public abstract DomanTestParameter[] getParameters();
}
