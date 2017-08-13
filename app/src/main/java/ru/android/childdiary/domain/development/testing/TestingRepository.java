package ru.android.childdiary.domain.development.testing;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.testing.data.TestResult;
import ru.android.childdiary.domain.development.testing.requests.TestResultsRequest;
import ru.android.childdiary.domain.development.testing.data.tests.core.Test;

public interface TestingRepository {
    Observable<Test> getTest(@NonNull TestType testType);

    Observable<List<Test>> getTests();

    Observable<List<TestResult>> getTestResults(@NonNull TestResultsRequest request);

    Observable<TestResult> add(@NonNull TestResult testResult);

    Observable<TestResult> delete(@NonNull TestResult testResult);

    Single<Boolean> checkDate(@NonNull Child child, @NonNull TestType testType, @NonNull DomanTestParameter testParameter, @NonNull LocalDate date);

    Observable<DomanTestParameter> getSelectedParameter(@NonNull TestType testType);

    Observable<DomanTestParameter> getSelectedParameterOnce(@NonNull TestType testType);

    Observable<DomanTestParameter> setSelectedParameter(@NonNull TestType testType, @NonNull DomanTestParameter testParameter);
}
