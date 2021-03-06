package ru.android.healthvector.domain.development.antropometry.validation;

import java.util.Collections;
import java.util.List;

import ru.android.healthvector.domain.core.validation.core.ValidationException;

public class AntropometryValidationException extends ValidationException {
    private final List<AntropometryValidationResult> results;

    public AntropometryValidationException(List<AntropometryValidationResult> results) {
        this.results = Collections.unmodifiableList(results);
    }

    @Override
    public List<AntropometryValidationResult> getValidationResults() {
        return results;
    }
}
