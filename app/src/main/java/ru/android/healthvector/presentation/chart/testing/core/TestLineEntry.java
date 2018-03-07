package ru.android.healthvector.presentation.chart.testing.core;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.domain.development.testing.data.processors.core.DomanResult;
import ru.android.healthvector.presentation.chart.core.LineEntry;

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
