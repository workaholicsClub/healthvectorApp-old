package ru.android.childdiary.presentation.chart.antropometry.core;

import android.support.annotation.NonNull;

import ru.android.childdiary.domain.interactors.development.antropometry.AntropometryPoint;
import ru.android.childdiary.presentation.chart.core.LineEntry;

class AntropometryLineEntrySimple extends LineEntry<AntropometryLineEntryInfo> {
    public AntropometryLineEntrySimple(float x, float y,
                                       @NonNull AntropometryPoint point) {
        super(x, y, null, null,
                AntropometryLineEntryInfo.builder()
                        .point(point)
                        .build());
    }

    public AntropometryPoint getValuePoint() {
        return getEntryInfo().getPoint();
    }
}
