package ru.android.childdiary.domain.interactors.development.testing;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.data.repositories.development.testing.TestingDataRepository;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.requests.HasDomanTestResultsResponse;
import ru.android.childdiary.domain.interactors.development.testing.requests.TestResultsRequest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.DomanTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;

public class TestingInteractor {
    private final TestingRepository testingRepository;

    @Inject
    public TestingInteractor(TestingDataRepository testingRepository) {
        this.testingRepository = testingRepository;
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

    public Observable<LinkedHashMap<DomanTestParameter, List<TestResult>>> getDomanTestResults(@NonNull Child child, @NonNull TestType testType) {
        return Observable.fromCallable(() -> {
            LinkedHashMap<DomanTestParameter, List<TestResult>> map = new LinkedHashMap<>();
            Test test = testingRepository.getTest(testType).blockingFirst();
            if (!(test instanceof DomanTest)) {
                return map;
            }
            DomanTestParameter[] parameters = ((DomanTest) test).getParameters();
            for (DomanTestParameter testParameter : parameters) {
                List<TestResult> testResults = getTestResults(TestResultsRequest.builder()
                        .child(child)
                        .testType(testType)
                        .testParameter(testParameter)
                        .build())
                        .blockingFirst();
                if (!testResults.isEmpty()) {
                    map.put(testParameter, testResults);
                }
            }
            return map;
        });
    }

    public Observable<HasDomanTestResultsResponse> hasDomanTestResults(@NonNull Child child) {
        return testingRepository.getTestResults(TestResultsRequest.builder()
                .child(child)
                .build())
                .map(this::hasData)
                .map(hasData -> HasDomanTestResultsResponse.builder()
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
}
