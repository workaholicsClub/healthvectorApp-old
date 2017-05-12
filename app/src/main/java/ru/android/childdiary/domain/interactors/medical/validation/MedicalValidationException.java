package ru.android.childdiary.domain.interactors.medical.validation;

import java.util.Collections;
import java.util.List;

import ru.android.childdiary.domain.core.ValidationException;

public class MedicalValidationException extends Exception implements ValidationException<MedicalValidationResult> {
    private final List<MedicalValidationResult> results;

    public MedicalValidationException(List<MedicalValidationResult> results) {
        this.results = Collections.unmodifiableList(results);
    }

    @Override
    public List<MedicalValidationResult> getValidationResults() {
        return results;
    }
}
