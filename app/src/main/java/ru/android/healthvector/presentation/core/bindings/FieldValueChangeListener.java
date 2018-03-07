package ru.android.healthvector.presentation.core.bindings;

import android.support.annotation.Nullable;

public interface FieldValueChangeListener<T> {
    void onValueChange(@Nullable T value);
}
