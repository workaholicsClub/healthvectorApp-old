package ru.android.childdiary.domain.interactors.core.exceptions;

public class TryCountExceededException extends Exception {
    public TryCountExceededException(String message) {
        super(message);
    }
}
