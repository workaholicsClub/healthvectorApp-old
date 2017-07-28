package ru.android.childdiary.domain.interactors.development.testing;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.data.repositories.development.testing.TestingDataRepository;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.core.HasDataResponse;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.requests.TestResultsRequest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.DomanTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;

public class TestingInteractor {
    private final TestingRepository testingRepository;

    @Inject
    public TestingInteractor(TestingDataRepository testingRepository) {
        this.testingRepository = testingRepository;
    }

    public Observable<Test> getTest(@NonNull TestType testType) {
        return testingRepository.getTest(testType);
    }

    public Observable<List<Test>> getTests() {
        return testingRepository.getTests();
    }

    public Observable<List<TestResult>> getTestResults(@NonNull TestResultsRequest request) {
        return testingRepository.getTestResults(request);
    }

    public Observable<TestResult> add(@NonNull TestResult testResult) {
        return testingRepository.add(testResult);
    }

    public Observable<TestResult> delete(@NonNull TestResult testResult) {
        return testingRepository.delete(testResult);
    }

    public Single<Boolean> checkDate(@NonNull Child child,
                                     @NonNull TestType testType,
                                     @NonNull DomanTestParameter testParameter,
                                     @NonNull LocalDate date) {
        return testingRepository.checkDate(child, testType, testParameter, date);
    }

    public Single<HasDataResponse> hasChartData(@NonNull Child child) {
        return testingRepository.getTestResults(TestResultsRequest.builder()
                .child(child)
                .build())
                .first(Collections.emptyList())
                .map(this::hasData)
                .map(hasData -> HasDataResponse.builder()
                        .child(child)
                        .hasData(hasData)
                        .build());
    }

    private boolean hasData(@NonNull List<TestResult> testResults) {
        return Observable.fromIterable(testResults)
                .filter(testResult -> testResult.getTest() instanceof DomanTest)
                .count()
                .map(count -> count > 0)
                .blockingGet();
    }

    public Observable<DomanTestParameter> getSelectedParameter(@NonNull TestType testType) {
        return testingRepository.getSelectedParameter(testType);
    }

    public Observable<DomanTestParameter> getSelectedParameterOnce(@NonNull TestType testType) {
        return testingRepository.getSelectedParameterOnce(testType);
    }

    public Observable<DomanTestParameter> setSelectedParameter(@NonNull TestType testType,
                                                               @NonNull DomanTestParameter testParameter) {
        return testingRepository.setSelectedParameter(testType, testParameter);
    }
}
