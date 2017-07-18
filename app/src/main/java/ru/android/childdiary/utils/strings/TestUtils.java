package ru.android.childdiary.utils.strings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.TestFactory;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.TestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.DomanTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;

public class TestUtils {
    @Nullable
    public static String getTestName(Context context, @Nullable TestType testType) {
        if (testType == null) {
            return null;
        }
        switch (testType) {
            case DOMAN_MENTAL:
                return context.getString(R.string.test_doman_mental_name);
            case DOMAN_PHYSICAL:
                return context.getString(R.string.test_doman_physical_name);
            case AUTISM:
                return context.getString(R.string.test_autism_name);
            case NEWBORN:
                return context.getString(R.string.test_newborn_name);
        }
        return null;
    }

    @Nullable
    public static String getTestDescription(Context context, @Nullable TestType testType) {
        if (testType == null) {
            return null;
        }
        switch (testType) {
            case DOMAN_PHYSICAL:
                return context.getString(R.string.test_doman_mental_description);
            case DOMAN_MENTAL:
                return context.getString(R.string.test_doman_physical_description);
            case AUTISM:
                return context.getString(R.string.test_autism_description);
            case NEWBORN:
                return context.getString(R.string.test_newborn_description);
        }
        return null;
    }

    @Nullable
    public static String toString(Context context, @Nullable DomanTestParameter testParameter) {
        if (testParameter == null) {
            return null;
        }
        switch (testParameter) {
            case MENTAL_VISION:
                return context.getString(R.string.mental_vision);
            case MENTAL_AUDITION:
                return context.getString(R.string.mental_audition);
            case MENTAL_SENSITIVITY:
                return context.getString(R.string.mental_sensitivity);
            case PHYSICAL_MOBILITY:
                return context.getString(R.string.physical_mobility);
            case PHYSICAL_SPEECH:
                return context.getString(R.string.physical_speech);
            case PHYSICAL_MANUAL:
                return context.getString(R.string.physical_manual);
        }
        return null;
    }

    @Nullable
    public static String getTestTitle(Context context, @NonNull Test test, @Nullable DomanTestParameter testParameter) {
        if (test instanceof DomanTest) {
            return toString(context, testParameter);
        }
        return test.getName();
    }

    @Nullable
    public static String getTestTitle(Context context, @NonNull TestResult testResult) {
        return getTestTitle(context, testResult.getTest(), testResult.getDomanTestParameter());
    }

    @Nullable
    public static String getTestResultShort(Context context, @NonNull TestResult testResult) {
        TestProcessor testProcessor = TestFactory.createTestProcessor(testResult);
        return testProcessor.interpretResultShort();
    }
}
