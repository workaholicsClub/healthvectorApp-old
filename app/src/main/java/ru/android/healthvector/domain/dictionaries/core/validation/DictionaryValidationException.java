package ru.android.healthvector.domain.dictionaries.core.validation;

import java.util.Collections;
import java.util.List;

import ru.android.healthvector.domain.core.validation.core.ValidationException;

public class DictionaryValidationException extends ValidationException {
    private final List<DictionaryValidationResult> results;

    public DictionaryValidationException(List<DictionaryValidationResult> results) {
        this.results = Collections.unmodifiableList(results);
    }

    @Override
    public List<DictionaryValidationResult> getValidationResults() {
        return results;
    }
}
