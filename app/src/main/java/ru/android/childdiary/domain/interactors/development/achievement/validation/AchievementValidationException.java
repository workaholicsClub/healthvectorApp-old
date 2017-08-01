package ru.android.childdiary.domain.interactors.development.achievement.validation;

import java.util.Collections;
import java.util.List;

import ru.android.childdiary.domain.core.validation.ValidationException;

public class AchievementValidationException extends ValidationException {
    private final List<AchievementValidationResult> results;

    public AchievementValidationException(List<AchievementValidationResult> results) {
        this.results = Collections.unmodifiableList(results);
    }

    @Override
    public List<AchievementValidationResult> getValidationResults() {
        return results;
    }
}
