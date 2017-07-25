package ru.android.childdiary.presentation.chart.testing.pages.core;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;
import ru.android.childdiary.presentation.chart.core.ChartState;

@Value
@Builder
public class DomanChartState implements ChartState {
    @NonNull
    TestType testType;
    @NonNull
    DomanTestParameter testParameter;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @NonNull
    List<DomanResult> testResults;

    @Override
    public boolean isEmpty() {
        return testResults.isEmpty();
    }
}
