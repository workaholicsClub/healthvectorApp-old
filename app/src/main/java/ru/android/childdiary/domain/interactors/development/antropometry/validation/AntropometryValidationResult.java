package ru.android.childdiary.domain.interactors.development.antropometry.validation;

import android.support.annotation.Nullable;

import lombok.Getter;
import ru.android.childdiary.domain.core.validation.ValidationResult;

public class AntropometryValidationResult extends ValidationResult {
    @Nullable
    @Getter
    private final AntropometryFieldType fieldType;

    public AntropometryValidationResult(@Nullable AntropometryFieldType fieldType) {
        this.fieldType = fieldType;
    }
}
