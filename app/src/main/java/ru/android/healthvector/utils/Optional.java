package ru.android.healthvector.utils;

import android.support.annotation.Nullable;

import java.util.NoSuchElementException;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode
public final class Optional<T> {
    private static final Optional EMPTY = new Optional<>();

    @Nullable
    private final T value;

    private Optional() {
        this.value = null;
    }

    private Optional(@Nullable T value) {
        this.value = value;
    }

    public static <T> Optional<T> empty() {
        //noinspection unchecked
        return EMPTY;
    }

    public static <T> Optional<T> of(@Nullable T value) {
        return value == null ? EMPTY : new Optional<>(value);
    }

    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public T or(@NonNull T other) {
        return value == null ? other : value;
    }

    @Nullable
    public T orNull() {
        return value;
    }

    @Override
    public String toString() {
        return value == null
                ? "Optional.empty"
                : String.format("Optional[%s]", value);
    }
}
