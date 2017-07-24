package ru.android.childdiary.presentation.chart.antropometry.core;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.domain.interactors.development.antropometry.AntropometryPoint;

@Value
@Builder
class AntropometryLineEntryInfo {
    @NonNull
    AntropometryPoint point;
}
