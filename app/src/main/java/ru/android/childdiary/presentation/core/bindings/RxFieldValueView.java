package ru.android.childdiary.presentation.core.bindings;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import ru.android.childdiary.presentation.core.fields.widgets.FieldValueView;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

public final class RxFieldValueView {
    private RxFieldValueView() {
        throw new AssertionError("No instances.");
    }

    @CheckResult
    @NonNull
    public static <T> FieldValueChangeEventsObservable<T> valueChangeEvents(
            @NonNull FieldValueView<T> view) {
        checkNotNull(view, "view == null");
        return new FieldValueChangeEventsObservable<>(view);
    }
}
