package ru.android.healthvector.utils.strings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.domain.development.testing.data.TestFactory;
import ru.android.healthvector.domain.development.testing.data.TestResult;
import ru.android.healthvector.domain.development.testing.data.interpreters.core.TestInterpreter;
import ru.android.healthvector.domain.development.testing.data.processors.core.BaseTestProcessor;
import ru.android.healthvector.domain.development.testing.data.processors.core.DomanResult;
import ru.android.healthvector.domain.development.testing.data.tests.core.DomanTest;
import ru.android.healthvector.domain.development.testing.data.tests.core.Test;

public class TestUtils {
    @Nullable
    public static String testParameter(Context context, @Nullable DomanTestParameter testParameter) {
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

    public static String getTestTitle(Context context, @NonNull Test test, @Nullable DomanTestParameter testParameter) {
        if (test instanceof DomanTest) {
            return testParameter(context, testParameter);
        }
        return test.getName();
    }

    @Nullable
    public static String getTestTitle(Context context, @NonNull TestResult testResult) {
        return getTestTitle(context, testResult.getTest(), testResult.getDomanTestParameter());
    }

    @Nullable
    public static String getTestResultShort(Context context, @NonNull TestResult testResult) {
        TestFactory testFactory = new TestFactory(context);
        BaseTestProcessor testProcessor = testFactory.createTestProcessor(testResult);
        TestInterpreter testInterpreter = testFactory.createTestInterpreter(testProcessor);
        return testInterpreter.interpretShort();
    }

    public static List<DomanResult> filterResults(@NonNull List<DomanResult> results) {
        return Observable.fromIterable(results)
                .filter(result -> result.getDomanDate() != null)
                .toList()
                .blockingGet();
    }

    public static boolean invalidResults(@NonNull List<TestResult> results) {
        return Observable.fromIterable(results)
                .filter(TestResult::isInvalid)
                .count()
                .map(count -> count > 0)
                .blockingGet();
    }
}
