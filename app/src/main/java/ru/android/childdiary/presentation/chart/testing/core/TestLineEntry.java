package ru.android.childdiary.presentation.chart.testing.core;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.domain.interactors.development.testing.data.processors.core.DomanResult;
import ru.android.childdiary.presentation.chart.core.LineEntry;

class TestLineEntry extends LineEntry<TestLineEntryInfo> {
    public TestLineEntry(float x, float y,
                         @NonNull Drawable normalIcon,
                         @NonNull Drawable selectedIcon,
                         @NonNull DomanTestParameter testParameter,
                         @NonNull DomanResult result) {
        super(x, y, normalIcon, selectedIcon,
                TestLineEntryInfo.builder()
                        .testParameter(testParameter)
                        .result(result)
                        .build());
    }

    public DomanTestParameter getTestParameter() {
        return getEntryInfo().getTestParameter();
    }

    public DomanResult getResult() {
        return getEntryInfo().getResult();
    }
}
