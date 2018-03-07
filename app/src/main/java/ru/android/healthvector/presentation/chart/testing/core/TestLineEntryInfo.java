package ru.android.healthvector.presentation.chart.testing.core;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.domain.development.testing.data.processors.core.DomanResult;
import ru.android.healthvector.presentation.chart.core.LineEntryInfo;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Value
@Builder
class TestLineEntryInfo extends LineEntryInfo {
    @NonNull
    DomanTestParameter testParameter;
    @NonNull
    DomanResult result;
}
