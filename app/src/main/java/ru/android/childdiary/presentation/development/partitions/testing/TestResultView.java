package ru.android.childdiary.presentation.development.partitions.testing;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.android.childdiary.domain.interactors.development.testing.Test;
import ru.android.childdiary.domain.interactors.development.testing.TestResult;
import ru.android.childdiary.presentation.core.AppPartitionView;

public interface TestResultView extends AppPartitionView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showTests(@NonNull List<Test> tests);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showTestResults(@NonNull List<TestResult> testResults);
}
