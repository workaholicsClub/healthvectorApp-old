package ru.android.childdiary.domain.calendar.validation;

import android.support.annotation.Nullable;

import lombok.Getter;
import ru.android.childdiary.domain.core.validation.core.ValidationResult;

public class CalendarValidationResult extends ValidationResult {
    @Nullable
    @Getter
    private final CalendarFieldType fieldType;

    public CalendarValidationResult() {
        this(null);
    }

    public CalendarValidationResult(@Nullable CalendarFieldType fieldType) {
        this.fieldType = fieldType;
    }
}