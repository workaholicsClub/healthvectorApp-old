package ru.android.childdiary.domain.calendar.validation;

import java.util.Collections;
import java.util.List;

import ru.android.childdiary.domain.core.validation.core.ValidationException;

public class CalendarValidationException extends ValidationException {
    private final List<CalendarValidationResult> results;

    public CalendarValidationException(List<CalendarValidationResult> results) {
        this.results = Collections.unmodifiableList(results);
    }

    @Override
    public List<CalendarValidationResult> getValidationResults() {
        return results;
    }
}
