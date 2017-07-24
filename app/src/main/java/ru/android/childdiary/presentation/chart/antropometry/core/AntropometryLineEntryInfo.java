package ru.android.childdiary.presentation.chart.antropometry.core;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;
import ru.android.childdiary.domain.interactors.development.antropometry.AntropometryPoint;
import ru.android.childdiary.presentation.chart.core.LineEntryInfo;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Value
@Builder
class AntropometryLineEntryInfo extends LineEntryInfo {
    @NonNull
    AntropometryPoint point;
}
