package ru.android.childdiary.domain.interactors.development.testing;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;

public interface TestingRepository {
    Observable<List<Test>> getTests();

    Observable<List<TestResult>> getTestResults(@NonNull Child child);
}
