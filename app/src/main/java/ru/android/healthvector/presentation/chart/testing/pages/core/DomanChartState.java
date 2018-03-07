package ru.android.healthvector.presentation.chart.testing.pages.core;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.data.types.TestType;
import ru.android.healthvector.domain.development.testing.data.processors.core.DomanResult;
import ru.android.healthvector.presentation.chart.core.ChartState;
import ru.android.healthvector.utils.strings.TestUtils;

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
