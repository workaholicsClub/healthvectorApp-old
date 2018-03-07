package ru.android.healthvector.presentation.chart.antropometry.core;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import ru.android.healthvector.domain.development.antropometry.data.AntropometryPoint;
import ru.android.healthvector.presentation.chart.core.LineEntry;

class AntropometryLineEntry extends LineEntry<AntropometryLineEntryInfo> {
    public AntropometryLineEntry(float x, float y,
                                 @NonNull Drawable normalIcon,
                                 @NonNull Drawable selectedIcon,
                                 @NonNull AntropometryPoint point) {
        super(x, y, normalIcon, selectedIcon,
                AntropometryLineEntryInfo.builder()
                        .point(point)
                        .build());
    }

    public AntropometryPoint getValuePoint() {
        return getEntryInfo().getPoint();
    }
}
