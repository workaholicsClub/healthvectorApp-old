package ru.android.childdiary.domain.core;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class ValidationResult {
    private final List<String> messages = new ArrayList<>();

    public boolean isValid() {
        return messages.isEmpty();
    }

    public boolean notValid() {
        return !isValid();
    }

    public void addMessage(@NonNull String message) {
        messages.add(message);
    }

    @Override
    public String toString() {
        return TextUtils.join("\n", messages);
    }
}
