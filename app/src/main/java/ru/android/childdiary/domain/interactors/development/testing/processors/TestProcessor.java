package ru.android.childdiary.domain.interactors.development.testing.processors;

import android.support.annotation.Nullable;

import ru.android.childdiary.data.types.TestType;

public interface TestProcessor {
    boolean isFinished();

    void goToNextQuestion();

    @Nullable
    static TestProcessor create(@Nullable TestType testType) {
        if (testType == null) {
            return null;
        }
        switch (testType) {
            case DOMAN_PHYSICAL:
                return new DomanPhysicalTestProcessor();
            case DOMAN_MENTAL:
                return new DomanMentalTestProcessor();
            case AUTISM:
                return new AutismTestProcessor();
            case NEWBORN:
                return new NewbornTestProcessor();
        }
        return null;
    }
}
