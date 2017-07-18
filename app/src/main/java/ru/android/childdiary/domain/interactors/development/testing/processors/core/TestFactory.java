package ru.android.childdiary.domain.interactors.development.testing.processors.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.processors.AutismTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.processors.DomanMentalTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.processors.DomanPhysicalTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.processors.NewbornTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.tests.AutismTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.DomanMentalTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.DomanPhysicalTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.NewbornTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;

public class TestFactory {
    public static BiTestProcessor createTestProcessor(@Nullable Test test,
                                                      @NonNull TestParameters parameters) {
        TestType testType = test == null ? null : test.getTestType();
        if (testType == null) {
            throw new IllegalStateException("Test type is null");
        }
        switch (testType) {
            case DOMAN_PHYSICAL:
                if (parameters.getBirthDate() != null && parameters.getDate() != null) {
                    return new DomanPhysicalTestProcessor((DomanPhysicalTest) test,
                            parameters.getParameter(),
                            parameters.getBirthDate(),
                            parameters.getDate());
                } else if (parameters.getAge() != null && parameters.getDate() != null) {
                    return new DomanPhysicalTestProcessor((DomanPhysicalTest) test,
                            parameters.getParameter(),
                            parameters.getAge(),
                            parameters.getDate());
                } else {
                    throw new IllegalStateException("Invalid test parameters: " + parameters);
                }
            case DOMAN_MENTAL:
                if (parameters.getBirthDate() != null && parameters.getDate() != null) {
                    return new DomanMentalTestProcessor((DomanMentalTest) test,
                            parameters.getParameter(),
                            parameters.getBirthDate(),
                            parameters.getDate());
                } else if (parameters.getAge() != null && parameters.getDate() != null) {
                    return new DomanMentalTestProcessor((DomanMentalTest) test,
                            parameters.getParameter(),
                            parameters.getAge(),
                            parameters.getDate());
                } else {
                    throw new IllegalStateException("Invalid test parameters: " + parameters);
                }
            case AUTISM:
                return new AutismTestProcessor((AutismTest) test);
            case NEWBORN:
                return new NewbornTestProcessor((NewbornTest) test);
        }
        throw new IllegalStateException("Unsupported test type");
    }

    public static TestProcessor createTestProcessor(@NonNull TestResult testResult) {
        TestProcessor testProcessor = TestFactory.createTestProcessor(testResult.getTest(), TestParameters.builder()
                .birthDate(testResult.getBirthDate())
                .date(testResult.getDate())
                .parameter(testResult.getDomanTestParameter())
                .build());
        testProcessor.setResult(testResult.getResult());
        return testProcessor;
    }
}
