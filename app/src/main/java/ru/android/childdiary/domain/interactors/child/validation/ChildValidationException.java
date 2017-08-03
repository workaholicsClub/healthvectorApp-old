package ru.android.childdiary.domain.interactors.child.validation;

import java.util.Collections;
import java.util.List;

import ru.android.childdiary.domain.interactors.core.validation.ValidationException;

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
