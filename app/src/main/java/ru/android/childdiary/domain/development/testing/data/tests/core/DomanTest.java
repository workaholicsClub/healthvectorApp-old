package ru.android.childdiary.domain.development.testing.data.tests.core;

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

    public DomanTest(@NonNull TestType testType,
                     @NonNull String nameEn,
                     @NonNull String nameRu,
                     @NonNull String descriptionEn,
                     @NonNull String descriptionRu,
                     @NonNull Map<DomanTestParameter, List<Question>> questions) {
        super(testType, nameEn, nameRu, descriptionEn, descriptionRu);
        this.questions = CollectionUtils.unmodifiableMap(questions);
    }

    public abstract DomanTestParameter[] getParameters();
}
