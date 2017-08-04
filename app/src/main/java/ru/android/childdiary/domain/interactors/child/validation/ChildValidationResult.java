package ru.android.childdiary.domain.interactors.child.validation;

import android.support.annotation.Nullable;

import lombok.Getter;
import ru.android.childdiary.domain.interactors.core.validation.core.ValidationResult;

public class ChildValidationResult extends ValidationResult {
    @Nullable
    @Getter
    private final ChildFieldType fieldType;

    public ChildValidationResult(@Nullable ChildFieldType fieldType) {
        this.fieldType = fieldType;
    }
}
