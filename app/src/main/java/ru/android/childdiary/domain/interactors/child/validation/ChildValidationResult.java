package ru.android.childdiary.domain.interactors.child.validation;

import android.support.annotation.Nullable;

import lombok.Getter;
import ru.android.childdiary.domain.core.ValidationResult;

public class ChildValidationResult extends ValidationResult {
    @Nullable
    @Getter
    private final ChildFieldType fieldType;

    public ChildValidationResult(ChildFieldType fieldType) {
        this.fieldType = fieldType;
    }
}
