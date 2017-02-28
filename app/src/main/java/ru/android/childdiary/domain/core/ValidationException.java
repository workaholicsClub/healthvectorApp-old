package ru.android.childdiary.domain.core;

import java.util.List;

public interface ValidationException<VR extends ValidationResult> {
    List<VR> getValidationResults();
}
