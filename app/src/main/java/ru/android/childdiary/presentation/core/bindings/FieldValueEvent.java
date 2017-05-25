package ru.android.childdiary.presentation.core.bindings;

import android.support.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import ru.android.childdiary.presentation.core.fields.widgets.FieldValueView;

@Value
@AllArgsConstructor(suppressConstructorProperties = true)
public class FieldValueEvent<T> {
    @NonNull
    FieldValueView<T> view;
    @Nullable
    T value;
}
