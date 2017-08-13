package ru.android.childdiary.domain.development.achievement.validation;

import android.support.annotation.Nullable;

import lombok.Getter;
import ru.android.childdiary.domain.core.validation.core.ValidationResult;

public class AchievementValidationResult extends ValidationResult {
    @Nullable
    @Getter
    private final AchievementFieldType fieldType;

    public AchievementValidationResult(@Nullable AchievementFieldType fieldType) {
        this.fieldType = fieldType;
    }
}
