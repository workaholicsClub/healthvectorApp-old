package ru.android.childdiary.presentation.chart.testing.pages.core;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.data.types.TestType;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;

@Value
@Builder
public class DomanChartState {
    @NonNull
    TestType testType;
    @NonNull
    DomanTestParameter testParameter;
    @NonNull
    List<DomanResult> testResults;
}
