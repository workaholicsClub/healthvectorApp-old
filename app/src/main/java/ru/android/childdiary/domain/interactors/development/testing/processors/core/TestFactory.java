package ru.android.childdiary.domain.interactors.development.testing.processors.core;

import android.support.annotation.Nullable;

import ru.android.childdiary.data.types.TestType;
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
    @Nullable
    public static TestProcessor createTestProcessor(@Nullable Test test) {
        TestType testType = test == null ? null : test.getTestType();
        if (testType == null) {
            return null;
        }
        switch (testType) {
            case DOMAN_PHYSICAL:
                return new DomanPhysicalTestProcessor((DomanPhysicalTest) test);
            case DOMAN_MENTAL:
                return new DomanMentalTestProcessor((DomanMentalTest) test);
            case AUTISM:
                return new AutismTestProcessor((AutismTest) test);
            case NEWBORN:
                return new NewbornTestProcessor((NewbornTest) test);
        }
        return null;
    }
}
