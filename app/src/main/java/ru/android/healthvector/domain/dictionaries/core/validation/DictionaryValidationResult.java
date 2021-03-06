package ru.android.healthvector.domain.dictionaries.core.validation;

import android.support.annotation.Nullable;

import lombok.Getter;
import ru.android.healthvector.domain.core.validation.core.ValidationResult;

public class DictionaryValidationResult extends ValidationResult {
    @Nullable
    @Getter
    private final DictionaryFieldType fieldType;

    public DictionaryValidationResult() {
        this(null);
    }

    public DictionaryValidationResult(@Nullable DictionaryFieldType fieldType) {
        this.fieldType = fieldType;
    }
}
