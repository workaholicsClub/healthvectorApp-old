package ru.android.childdiary.domain.core.validation;

import java.util.Collections;
import java.util.List;

import ru.android.childdiary.domain.core.validation.core.ValidationException;

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
