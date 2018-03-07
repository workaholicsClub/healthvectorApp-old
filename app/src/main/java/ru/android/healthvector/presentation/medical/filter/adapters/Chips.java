package ru.android.healthvector.presentation.medical.filter.adapters;

import android.content.Context;
import android.support.annotation.Nullable;

public interface Chips {
    @Nullable
    String getText(Context context);

    boolean sameAs(Chips other);
}
