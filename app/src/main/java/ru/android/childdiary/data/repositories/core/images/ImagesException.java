package ru.android.childdiary.data.repositories.core.images;

public class ImagesException extends RuntimeException {
    public ImagesException(String message) {
        super(message);
    }

    public ImagesException(String message, Throwable cause) {
        super(message, cause);
    }
}
