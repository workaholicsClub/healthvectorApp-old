package ru.android.healthvector.presentation.chart.antropometry.core;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import ru.android.healthvector.domain.development.antropometry.data.AntropometryPoint;
import ru.android.healthvector.presentation.chart.core.LineEntryInfo;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Value
@Builder
class AntropometryLineEntryInfo extends LineEntryInfo {
    @NonNull
    AntropometryPoint point;
}
