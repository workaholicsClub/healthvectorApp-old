package ru.android.childdiary.data.db.converters;


import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.requery.Converter;
import io.requery.Nullable;

public abstract class SimpleConverter<T> implements Converter<T, String> {
    private static final String NULL = "null";

    private final Logger logger = LoggerFactory.getLogger(toString());

    protected static <E> String toString(@android.support.annotation.Nullable E value) {
        return value == null ? NULL : value.toString();
    }

    @android.support.annotation.Nullable
    protected static Integer toInteger(@NonNull String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @android.support.annotation.Nullable
    public static <T extends Enum<T>> T toEnum(Class<T> enumType, @NonNull String name) {
        try {
            return T.valueOf(enumType, name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public Class<String> getPersistedType() {
        return String.class;
    }

    @Nullable
    @Override
    public Integer getPersistedSize() {
        return null;
    }

    @Override
    public String convertToPersisted(T value) {
        return value == null ? null : map(value);
    }

    @Override
    @android.support.annotation.Nullable
    public T convertToMapped(Class<? extends T> type, String value) {
        try {
            return value == null ? null : map(value);
        } catch (Exception e) {
            return null;
        }
    }

    @android.support.annotation.Nullable
    protected abstract String map(@NonNull T value);

    @android.support.annotation.Nullable
    protected abstract T map(@NonNull String value);
}
