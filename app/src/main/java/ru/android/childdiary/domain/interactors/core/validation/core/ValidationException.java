package ru.android.childdiary.domain.interactors.core.validation.core;

import java.util.List;

public abstract class ValidationException extends Exception {
    public abstract List<? extends ValidationResult> getValidationResults();
}
