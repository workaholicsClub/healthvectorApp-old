package ru.android.childdiary.presentation.chart.antropometry.core;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;

import ru.android.childdiary.domain.interactors.development.antropometry.AntropometryPoint;

class AntropometryLineEntry extends Entry {
    private final AntropometryLineEntryInfo entryInfo;
    private final Drawable normalIcon, selectedIcon;

    public AntropometryLineEntry(float x, float y,
                                 @NonNull Drawable normalIcon,
                                 @NonNull Drawable selectedIcon,
                                 @NonNull AntropometryPoint point) {
        this(x, y, normalIcon, selectedIcon,
                AntropometryLineEntryInfo.builder()
                        .point(point)
                        .build());
    }

    private AntropometryLineEntry(float x, float y,
                                  @NonNull Drawable normalIcon,
                                  @NonNull Drawable selectedIcon,
                                  @NonNull AntropometryLineEntryInfo entryInfo) {
        super(x, y, normalIcon, entryInfo);
        this.entryInfo = entryInfo;
        this.normalIcon = normalIcon;
        this.selectedIcon = selectedIcon;
    }

    public AntropometryPoint getValuePoint() {
        return entryInfo.getPoint();
    }

    public void select() {
        setIcon(selectedIcon);
    }

    public void deselect() {
        setIcon(normalIcon);
    }
}
