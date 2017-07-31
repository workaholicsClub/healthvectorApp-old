package ru.android.childdiary.domain.core.validation;

import java.util.List;

public abstract class ValidationException extends Exception {
    public abstract List<? extends ValidationResult> getValidationResults();
}
