package ru.android.healthvector.domain.development.antropometry.validation;

import android.support.annotation.Nullable;

import lombok.Getter;
import ru.android.healthvector.domain.core.validation.core.ValidationResult;

public class AntropometryValidationResult extends ValidationResult {
    @Nullable
    @Getter
    private final AntropometryFieldType fieldType;

    public AntropometryValidationResult(@Nullable AntropometryFieldType fieldType) {
        this.fieldType = fieldType;
    }
}
