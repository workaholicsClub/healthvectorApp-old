package ru.android.childdiary.data.repositories.core.exceptions;

public class RestrictDeleteException extends RuntimeException {
    public RestrictDeleteException(String message) {
        super(message);
    }
}
