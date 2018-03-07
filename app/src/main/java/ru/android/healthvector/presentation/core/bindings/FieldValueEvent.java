package ru.android.healthvector.presentation.core.bindings;

import android.support.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor(suppressConstructorProperties = true)
public class FieldValueEvent<T> {
    @NonNull
    FieldValueView<T> view;
    @Nullable
    T value;
}
