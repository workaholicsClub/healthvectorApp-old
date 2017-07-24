package ru.android.childdiary.presentation.chart.testing.core;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;

@Value
@Builder
class TestLineEntryInfo {
    @NonNull
    DomanTestParameter testParameter;
    @NonNull
    DomanResult result;
}
