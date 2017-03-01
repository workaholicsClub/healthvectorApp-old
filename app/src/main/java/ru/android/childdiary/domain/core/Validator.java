package ru.android.childdiary.domain.core;

import android.support.annotation.NonNull;

import java.util.List;

public abstract class Validator<T, VR extends ValidationResult> {
    public boolean isValid(List<VR> results) {
        for (VR result : results) {
            if (!result.isValid()) {
                return false;
            }
        }
        return true;
    }

    public abstract List<VR> validate(@NonNull T item);
}
