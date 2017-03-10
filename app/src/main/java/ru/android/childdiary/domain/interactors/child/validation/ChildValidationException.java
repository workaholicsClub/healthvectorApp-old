package ru.android.childdiary.domain.interactors.child.validation;

import java.util.Collections;
import java.util.List;

import ru.android.childdiary.domain.core.ValidationException;
import ru.android.childdiary.domain.interactors.child.validation.ChildValidationResult;

public class ChildValidationException extends Exception implements ValidationException<ChildValidationResult> {
    private final List<ChildValidationResult> results;

    public ChildValidationException(List<ChildValidationResult> results) {
        this.results = Collections.unmodifiableList(results);
    }

    @Override
    public List<ChildValidationResult> getValidationResults() {
        return results;
    }
}
