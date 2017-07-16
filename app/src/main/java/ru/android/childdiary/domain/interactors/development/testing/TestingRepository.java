package ru.android.childdiary.domain.interactors.development.testing;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;

public interface TestingRepository {
    Observable<Test> getTest(@NonNull TestType testType);

    Observable<List<Test>> getTests();

    Observable<List<TestResult>> getTestResults(@NonNull Child child);

    Observable<TestResult> add(@NonNull TestResult testResult);

    Observable<TestResult> delete(@NonNull TestResult testResult);
}
