package ru.android.childdiary.presentation.development.partitions.testing;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.joda.time.LocalDate;

import java.util.List;

import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Test;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.presentation.core.AppPartitionView;

public interface TestResultView extends AppPartitionView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showTests(@NonNull List<Test> tests);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showTestResults(@NonNull List<TestResult> testResults);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void navigateToTest(@NonNull Test test, @NonNull Child child, @NonNull LocalDate date);
}
