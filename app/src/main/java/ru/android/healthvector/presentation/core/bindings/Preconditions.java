package ru.android.healthvector.presentation.core.bindings;

import android.os.Looper;

import io.reactivex.Observer;

public class Preconditions {
    public static void checkNotNull(Object value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean checkMainThread(Observer<?> observer) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            observer.onError(new IllegalStateException(
                    "Expected to be called on the main thread but was "
                            + Thread.currentThread().getName()));
            return false;
        }
        return true;
    }
}
