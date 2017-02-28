package ru.android.childdiary.domain.core;

import android.support.annotation.NonNull;

import java.util.List;

public abstract class Validator<T, VR extends ValidationResult> {
    public abstract List<VR> validate(@NonNull T item);

    protected void addResult(@NonNull List<VR> results, @NonNull VR result) {
        if (!result.isValid()) {
            results.add(result);
        }
    }
}
