package ru.android.childdiary.domain.interactors.development.antropometry.validation;

import java.util.Collections;
import java.util.List;

import ru.android.childdiary.domain.core.validation.ValidationException;

public class AntropometryValidationException extends Exception implements ValidationException<AntropometryValidationResult> {
    private final List<AntropometryValidationResult> results;

    public AntropometryValidationException(List<AntropometryValidationResult> results) {
        this.results = Collections.unmodifiableList(results);
    }

    @Override
    public List<AntropometryValidationResult> getValidationResults() {
        return results;
    }
}
