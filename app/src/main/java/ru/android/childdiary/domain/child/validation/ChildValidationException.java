package ru.android.childdiary.domain.child.validation;

import java.util.Collections;
import java.util.List;

import ru.android.childdiary.domain.core.validation.core.ValidationException;

public class ChildValidationException extends ValidationException {
    private final List<ChildValidationResult> results;

    public ChildValidationException(List<ChildValidationResult> results) {
        this.results = Collections.unmodifiableList(results);
    }

    @Override
    public List<ChildValidationResult> getValidationResults() {
        return results;
    }
}
