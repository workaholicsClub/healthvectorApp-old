package ru.android.childdiary.domain.interactors.development.achievement.validation;

import android.support.annotation.Nullable;

import lombok.Getter;
import ru.android.childdiary.domain.core.validation.ValidationResult;

public class AchievementValidationResult extends ValidationResult {
    @Nullable
    @Getter
    private final AchievementFieldType fieldType;

    public AchievementValidationResult(@Nullable AchievementFieldType fieldType) {
        this.fieldType = fieldType;
    }
}
