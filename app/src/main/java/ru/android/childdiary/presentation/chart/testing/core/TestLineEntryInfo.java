package ru.android.childdiary.presentation.chart.testing.core;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;
import ru.android.childdiary.presentation.chart.core.LineEntryInfo;

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
