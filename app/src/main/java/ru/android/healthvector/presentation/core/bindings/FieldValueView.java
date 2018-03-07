package ru.android.healthvector.presentation.core.bindings;

import android.support.annotation.NonNull;

public interface FieldValueView<T> {
    T getValue();

    void addValueChangeListener(@NonNull FieldValueChangeListener<T> listener);

    void removeValueChangeListener(@NonNull FieldValueChangeListener<T> listener);
}
