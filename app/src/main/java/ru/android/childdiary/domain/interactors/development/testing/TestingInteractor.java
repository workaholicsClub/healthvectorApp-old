package ru.android.childdiary.domain.interactors.development.testing;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.development.testing.TestingDataRepository;
import ru.android.childdiary.domain.interactors.child.Child;
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

    public Observable<List<TestResult>> getTestResults(@NonNull Child child) {
        return testingRepository.getTestResults(child);
    }
}
