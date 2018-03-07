package ru.android.healthvector.presentation.chart.core;

import android.content.Context;
import android.support.annotation.Nullable;

public interface ValueFormatter {
    String format(Context context, @Nullable Double d);
}
