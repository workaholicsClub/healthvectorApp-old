package ru.android.childdiary.presentation.testing.dynamic;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;

@Value
@Builder
public class DomanChartState {
    @NonNull
    DomanTestParameter testParameter;
    @NonNull
    List<DomanResult> testResults;
}
