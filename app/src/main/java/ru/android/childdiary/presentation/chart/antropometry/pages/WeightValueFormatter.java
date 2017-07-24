package ru.android.childdiary.presentation.chart.antropometry.pages;

import android.content.Context;
import android.support.annotation.Nullable;

import ru.android.childdiary.presentation.chart.core.ValueFormatter;
import ru.android.childdiary.utils.strings.DoubleUtils;

class WeightValueFormatter implements ValueFormatter {
    @Override
    public String format(Context context, @Nullable Double d) {
        return DoubleUtils.weightReview(context, d);
    }
}
