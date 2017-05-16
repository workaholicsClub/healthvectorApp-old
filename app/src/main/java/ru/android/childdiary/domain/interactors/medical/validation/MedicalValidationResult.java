package ru.android.childdiary.domain.interactors.medical.validation;

import android.support.annotation.Nullable;

import lombok.Getter;
import ru.android.childdiary.domain.core.validation.ValidationResult;

public class MedicalValidationResult extends ValidationResult {
    @Nullable
    @Getter
    private final MedicalFieldType fieldType;

    public MedicalValidationResult() {
        this(null);
    }

    public MedicalValidationResult(@Nullable MedicalFieldType fieldType) {
        this.fieldType = fieldType;
    }
}
