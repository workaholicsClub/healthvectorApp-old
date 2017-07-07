package ru.android.childdiary.data.repositories.development.testing;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.Test;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.domain.interactors.development.testing.TestingRepository;

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
        Test domanPhysical = Test.builder()
                .testType(TestType.DOMAN_PHYSICAL)
                .name(context.getString(R.string.test_doman_physical_name))
                .build();
        tests.add(domanPhysical);
        Test domanMental = Test.builder()
                .testType(TestType.DOMAN_MENTAL)
                .name(context.getString(R.string.test_doman_mental_name))
                .build();
        tests.add(domanMental);
        Test newborn = Test.builder()
                .testType(TestType.NEWBORN)
                .name(context.getString(R.string.test_newborn_name))
                .build();
        tests.add(newborn);
        Test autism = Test.builder()
                .testType(TestType.AUTISM)
                .name(context.getString(R.string.test_autism_name))
                .build();
        tests.add(autism);
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
