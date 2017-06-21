package ru.android.childdiary.domain.core;

public class TryCountExceededException extends Exception {
    public TryCountExceededException(String message) {
        super(message);
    }
}
