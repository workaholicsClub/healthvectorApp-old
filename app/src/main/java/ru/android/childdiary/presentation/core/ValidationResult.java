package ru.android.childdiary.presentation.core;

import lombok.Getter;

@Getter
public class ValidationResult {
    private String message = "";
    private boolean isValid = true;

    public void failWithMessage(String message) {
        isValid = false;
        this.message += (this.message.length() > 0 ? "\n" : "") + message;
    }
}
