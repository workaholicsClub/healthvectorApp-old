package ru.android.childdiary.domain.interactors.development.testing.data.tests.core;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.Collections;
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
import ru.android.childdiary.utils.CollectionUtils;

@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class DomanTest extends Test {
    public static final int MAX_YEARS = 18;

    @NonNull
    Map<DomanTestParameter, List<Question>> questions;
    @NonNull
    String resultTextFormat;
    @NonNull
    String advanced, normal, slow;
    @NonNull
    List<String> stageTitles, stageDescriptions;

    public DomanTest(@NonNull TestType testType,
                     @NonNull String name,
                     @NonNull String description,
                     @NonNull Map<DomanTestParameter, List<Question>> questions,
                     @NonNull String resultTextFormat,
                     @NonNull String advanced, @NonNull String normal, @NonNull String slow,
                     @NonNull List<String> stageTitles,
                     @NonNull List<String> stageDescriptions) {
        super(testType, name, description);
        this.questions = CollectionUtils.unmodifiableMap(questions);
        this.resultTextFormat = resultTextFormat;
        this.advanced = advanced;
        this.normal = normal;
        this.slow = slow;
        this.stageTitles = Collections.unmodifiableList(stageTitles);
        this.stageDescriptions = Collections.unmodifiableList(stageDescriptions);
    }

    public String getStageType(int initialStage, int stage, @Nullable LocalDate domanDate) {
        if (domanDate == null) {
            return slow;
        }
        if (initialStage == stage) {
            return normal;
        } else if (initialStage < stage) {
            return advanced;
        }
        return slow;
    }

    @Nullable
    public String getStageTitle(int stage) {
        int index = stage;
        if (index >= 0 && index < stageTitles.size()) {
            return stageTitles.get(index);
        }
        return null;
    }

    @Nullable
    public String getStageDescription(int stage) {
        int index = stage;
        if (index >= 0 && index < stageDescriptions.size()) {
            return stageDescriptions.get(index);
        }
        return null;
    }

    public abstract DomanTestParameter[] getParameters();
}
