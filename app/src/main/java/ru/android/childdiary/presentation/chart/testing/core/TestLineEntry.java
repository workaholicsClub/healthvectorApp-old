package ru.android.childdiary.presentation.chart.testing.core;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;

import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.DomanResult;

class TestLineEntry extends Entry {
    private final TestLineEntryInfo entryInfo;
    private final Drawable normalIcon, selectedIcon;

    public TestLineEntry(float x, float y,
                         @NonNull Drawable normalIcon,
                         @NonNull Drawable selectedIcon,
                         @NonNull DomanTestParameter testParameter,
                         @NonNull DomanResult result) {
        this(x, y, normalIcon, selectedIcon,
                TestLineEntryInfo.builder()
                        .testParameter(testParameter)
                        .result(result)
                        .build());
    }

    private TestLineEntry(float x, float y,
                          @NonNull Drawable normalIcon,
                          @NonNull Drawable selectedIcon,
                          @NonNull TestLineEntryInfo entryInfo) {
        super(x, y, normalIcon, entryInfo);
        this.entryInfo = entryInfo;
        this.normalIcon = normalIcon;
        this.selectedIcon = selectedIcon;
    }

    public DomanTestParameter getTestParameter() {
        return entryInfo.getTestParameter();
    }

    public DomanResult getResult() {
        return entryInfo.getResult();
    }

    public void select() {
        setIcon(selectedIcon);
    }

    public void deselect() {
        setIcon(normalIcon);
    }
}
