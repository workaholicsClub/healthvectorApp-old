package ru.android.childdiary.domain.development.testing.data;

import android.content.Context;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import javax.inject.Inject;

import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.development.testing.data.interpreters.AutismTestInterpreter;
import ru.android.childdiary.domain.development.testing.data.interpreters.DomanMentalTestInterpreter;
import ru.android.childdiary.domain.development.testing.data.interpreters.DomanPhysicalTestInterpreter;
import ru.android.childdiary.domain.development.testing.data.interpreters.NewbornTestInterpreter;
import ru.android.childdiary.domain.development.testing.data.interpreters.core.TestInterpreter;
import ru.android.childdiary.domain.development.testing.data.processors.AutismTestProcessor;
import ru.android.childdiary.domain.development.testing.data.processors.DomanMentalTestProcessor;
import ru.android.childdiary.domain.development.testing.data.processors.DomanPhysicalTestProcessor;
import ru.android.childdiary.domain.development.testing.data.processors.NewbornTestProcessor;
import ru.android.childdiary.domain.development.testing.data.processors.core.BaseTestProcessor;
import ru.android.childdiary.domain.development.testing.data.processors.core.DomanTestProcessor;
import ru.android.childdiary.domain.development.testing.data.processors.core.TestParameters;
import ru.android.childdiary.domain.development.testing.data.tests.AutismTest;
import ru.android.childdiary.domain.development.testing.data.tests.DomanMentalTest;
import ru.android.childdiary.domain.development.testing.data.tests.DomanPhysicalTest;
import ru.android.childdiary.domain.development.testing.data.tests.NewbornTest;
import ru.android.childdiary.domain.development.testing.data.tests.core.Test;

public class TestFactory {
    private final Context context;

    @Inject
    public TestFactory(Context context) {
        this.context = context;
    }

    public BaseTestProcessor createTestProcessor(@NonNull Test test,
                                                 @NonNull TestParameters parameters) {
        TestType testType = test.getTestType();
        switch (testType) {
            case DOMAN_PHYSICAL:
                if (parameters.getBirthDate() != null && parameters.getDate() != null) {
                    return new DomanPhysicalTestProcessor((DomanPhysicalTest) test,
                            parameters.getParameter(),
                            parameters.getBirthDate(),
                            parameters.getDate());
                } else if (parameters.getAge() != null && parameters.getDate() != null) {
                    LocalDate birthDate = parameters.getDate().minusMonths(parameters.getAge().getMonths());
                    return new DomanPhysicalTestProcessor((DomanPhysicalTest) test,
                            parameters.getParameter(),
                            birthDate,
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
                    LocalDate birthDate = parameters.getDate().minusMonths(parameters.getAge().getMonths());
                    return new DomanMentalTestProcessor((DomanMentalTest) test,
                            parameters.getParameter(),
                            birthDate,
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

    public BaseTestProcessor createTestProcessor(@NonNull TestResult testResult) {
        BaseTestProcessor testProcessor = createTestProcessor(testResult.getTest(), TestParameters.builder()
                .birthDate(testResult.getBirthDate())
                .date(testResult.getDate())
                .parameter(testResult.getDomanTestParameter())
                .build());
        testProcessor.setResult(testResult.getResult());
        if (testProcessor instanceof DomanTestProcessor) {
            ((DomanTestProcessor) testProcessor).setDomanDate(testResult.getDomanDate());
        }
        return testProcessor;
    }

    public TestInterpreter createTestInterpreter(@NonNull BaseTestProcessor testProcessor) {
        TestType testType = testProcessor.getTest().getTestType();
        switch (testType) {
            case DOMAN_PHYSICAL:
                return new DomanPhysicalTestInterpreter(context, (DomanPhysicalTestProcessor) testProcessor);
            case DOMAN_MENTAL:
                return new DomanMentalTestInterpreter(context, (DomanMentalTestProcessor) testProcessor);
            case AUTISM:
                return new AutismTestInterpreter(context, (AutismTestProcessor) testProcessor);
            case NEWBORN:
                return new NewbornTestInterpreter(context, (NewbornTestProcessor) testProcessor);
        }
        throw new IllegalStateException("Unsupported test type");
    }
}
