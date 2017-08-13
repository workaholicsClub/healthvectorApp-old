package ru.android.childdiary.presentation.chart.testing.pages.core;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.development.testing.data.processors.core.DomanResult;
import ru.android.childdiary.presentation.chart.core.ChartState;
import ru.android.childdiary.utils.strings.TestUtils;

@Value
public class DomanChartState implements ChartState {
    @NonNull
    TestType testType;
    @NonNull
    DomanTestParameter testParameter;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @NonNull
    List<DomanResult> testResults;
    @NonNull
    List<DomanResult> filteredTestResults;
    boolean invalidResults;

    @Builder
    public DomanChartState(@NonNull TestType testType,
                           @NonNull DomanTestParameter testParameter,
                           @NonNull List<DomanResult> testResults,
                           boolean invalidResults) {
        this.testType = testType;
        this.testParameter = testParameter;
        this.testResults = testResults;
        this.filteredTestResults = TestUtils.filterResults(testResults);
        this.invalidResults = invalidResults;
    }

    @Override
    public boolean isEmpty() {
        return testResults.isEmpty();
    }

    @Override
    public boolean noChartData() {
        return filteredTestResults.isEmpty();
    }
}
