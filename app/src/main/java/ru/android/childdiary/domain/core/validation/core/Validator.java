package ru.android.childdiary.domain.core.validation.core;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;

public abstract class Validator<T, VR extends ValidationResult> {
    public final boolean isValid(@NonNull List<VR> results) {
        for (VR result : results) {
            if (!result.isValid()) {
                return false;
            }
        }
        return true;
    }

    public final Observable<T> validateObservable(@NonNull T item) {
        return Observable.defer(() -> {
            List<VR> results = validate(item);
            if (!isValid(results)) {
                return Observable.error(createException(results));
            }
            return Observable.just(item);
        }).map(this::fix);
    }

    public abstract List<VR> validate(@NonNull T item);

    public List<VR> validateOnUi(@NonNull T item) {
        return validate(item);
    }

    protected abstract ValidationException createException(@NonNull List<VR> results);

    protected T fix(@NonNull T item) {
        return item;
    }
}
