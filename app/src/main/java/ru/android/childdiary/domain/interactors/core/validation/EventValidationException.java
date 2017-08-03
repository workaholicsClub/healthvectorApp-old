package ru.android.childdiary.domain.interactors.core.validation;

import java.util.Collections;
import java.util.List;

public class EventValidationException extends ValidationException {
    private final List<EventValidationResult> results;

    public EventValidationException(List<EventValidationResult> results) {
        this.results = Collections.unmodifiableList(results);
    }

    @Override
    public List<EventValidationResult> getValidationResults() {
        return results;
    }
}
