package ru.android.childdiary.domain.interactors.child;

import lombok.Getter;
import ru.android.childdiary.domain.core.ValidationResult;

public class ChildValidationResult extends ValidationResult {
    @Getter
    private final ChildFieldType fieldType;

    public ChildValidationResult(ChildFieldType fieldType) {
        this.fieldType = fieldType;
    }
}
