package ru.android.healthvector.presentation.core.bindings;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

public final class RxFieldValueView {
    private RxFieldValueView() {
        throw new AssertionError("No instances.");
    }

    @CheckResult
    @NonNull
    public static <T> FieldValueChangeEventsObservable<T> valueChangeEvents(
            @NonNull FieldValueView<T> view) {
        Preconditions.checkNotNull(view, "view == null");
        return new FieldValueChangeEventsObservable<>(view);
    }
}
