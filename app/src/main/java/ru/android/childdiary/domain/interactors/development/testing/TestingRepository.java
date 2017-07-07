package ru.android.childdiary.domain.interactors.development.testing;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.child.Child;

public interface TestingRepository {
    Observable<List<Test>> getTests();

    Observable<List<TestResult>> getTestResults(@NonNull Child child);
}
