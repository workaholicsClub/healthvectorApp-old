package ru.android.childdiary.domain.core.exceptions;

public class TryCountExceededException extends Exception {
    public TryCountExceededException(String message) {
        super(message);
    }
}
