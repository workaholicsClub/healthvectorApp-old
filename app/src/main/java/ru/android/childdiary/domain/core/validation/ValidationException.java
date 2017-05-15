package ru.android.childdiary.domain.core.validation;

import java.util.List;

public interface ValidationException<VR extends ValidationResult> {
    List<VR> getValidationResults();
}
