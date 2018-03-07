package ru.android.healthvector.domain.core.validation;

import android.support.annotation.Nullable;

import lombok.Getter;
import ru.android.healthvector.domain.core.validation.core.ValidationResult;

public class EventValidationResult extends ValidationResult {
    @Nullable
    @Getter
    private final EventFieldType fieldType;

    public EventValidationResult() {
        this(null);
    }

    public EventValidationResult(@Nullable EventFieldType fieldType) {
        this.fieldType = fieldType;
    }
}
