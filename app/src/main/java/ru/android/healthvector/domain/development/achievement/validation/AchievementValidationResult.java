package ru.android.healthvector.domain.development.achievement.validation;

import android.support.annotation.Nullable;

import lombok.Getter;
import ru.android.healthvector.domain.core.validation.core.ValidationResult;

public class AchievementValidationResult extends ValidationResult {
    @Nullable
    @Getter
    private final AchievementFieldType fieldType;

    public AchievementValidationResult(@Nullable AchievementFieldType fieldType) {
        this.fieldType = fieldType;
    }
}
