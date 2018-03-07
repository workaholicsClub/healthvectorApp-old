package ru.android.healthvector.presentation.chart.antropometry.pages;

import android.content.Context;
import android.support.annotation.Nullable;

import ru.android.healthvector.presentation.chart.core.ValueFormatter;
import ru.android.healthvector.utils.strings.DoubleUtils;

class WeightValueFormatter implements ValueFormatter {
    @Override
    public String format(Context context, @Nullable Double d) {
        return DoubleUtils.weightReview(context, d);
    }
}
