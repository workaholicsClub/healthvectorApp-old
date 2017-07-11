package ru.android.childdiary.data.repositories.development.testing;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.Test;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.TestingRepository;
import ru.android.childdiary.utils.strings.TestUtils;

@Singleton
public class TestingDataRepository implements TestingRepository {
    private final Context context;
    private final TestingDbService testingDbService;
    private final List<Test> tests = new ArrayList<>();

    @Inject
    public TestingDataRepository(Context context, TestingDbService testingDbService) {
        this.context = context;
        this.testingDbService = testingDbService;
        initTests();
    }

    private void initTests() {
        for (TestType testType : TestType.values()) {
            Test test = Test.builder()
                    .testType(testType)
                    .name(TestUtils.getTestName(context, testType))
                    .description(TestUtils.getTestDescription(context, testType))
                    .build();
            tests.add(test);
        }
    }

    @Override
    public Observable<List<Test>> getTests() {
        return Observable.just(tests);
    }

    @Override
    public Observable<List<TestResult>> getTestResults(@NonNull Child child) {
        return testingDbService.getTestResults(child);
    }
}
