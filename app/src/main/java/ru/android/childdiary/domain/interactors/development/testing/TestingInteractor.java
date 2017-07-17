package ru.android.childdiary.domain.interactors.development.testing;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.data.repositories.development.testing.TestingDataRepository;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.child.Child;
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

    public Observable<List<TestResult>> getTestResults(@NonNull Child child) {
        return testingRepository.getTestResults(child);
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
}
