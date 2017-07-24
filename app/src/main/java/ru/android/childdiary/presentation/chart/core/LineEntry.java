package ru.android.childdiary.presentation.chart.core;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.data.Entry;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class LineEntry<T extends LineEntryInfo> extends Entry {
    @Nullable
    private final Drawable normalIcon, selectedIcon;
    @Getter
    @NonNull
    private final T entryInfo;

    protected LineEntry(float x, float y,
                        @Nullable Drawable normalIcon,
                        @Nullable Drawable selectedIcon,
                        @NonNull T entryInfo) {
        super(x, y, normalIcon, entryInfo);
        this.normalIcon = normalIcon;
        this.selectedIcon = selectedIcon;
        this.entryInfo = entryInfo;
    }

    public void select() {
        if (selectedIcon != null) {
            setIcon(selectedIcon);
        }
    }

    public void deselect() {
        if (normalIcon != null) {
            setIcon(normalIcon);
        }
    }
}
