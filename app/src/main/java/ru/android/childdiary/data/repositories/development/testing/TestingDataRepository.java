package ru.android.childdiary.data.repositories.development.testing;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.TestingRepository;
import ru.android.childdiary.domain.interactors.development.testing.tests.AutismTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.DomanMentalTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.DomanPhysicalTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.NewbornTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;
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
            Test test = createTest(testType);
            tests.add(test);
        }
    }

    @Nullable
    private Test createTest(@Nullable TestType testType) {
        if (testType == null) {
            return null;
        }
        switch (testType) {
            case DOMAN_PHYSICAL:
                return DomanPhysicalTest.builder()
                        .name(TestUtils.getTestName(context, testType))
                        .description(TestUtils.getTestDescription(context, testType))
                        .build();
            case DOMAN_MENTAL:
                return DomanMentalTest.builder()
                        .name(TestUtils.getTestName(context, testType))
                        .description(TestUtils.getTestDescription(context, testType))
                        .build();
            case AUTISM:
                return AutismTest.builder()
                        .name(TestUtils.getTestName(context, testType))
                        .description(TestUtils.getTestDescription(context, testType))
                        .questions(Collections.emptyList())
                        .build();
            case NEWBORN:
                return NewbornTest.builder()
                        .name(TestUtils.getTestName(context, testType))
                        .description(TestUtils.getTestDescription(context, testType))
                        .questions(Collections.emptyList())
                        .build();
        }
        return null;
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
