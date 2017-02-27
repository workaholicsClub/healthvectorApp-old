package ru.android.childdiary.presentation.core;

import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private final List<String> messages = new ArrayList<>();

    public boolean isValid() {
        return messages.isEmpty();
    }

    public void addMessage(@NonNull String message) {
        messages.add(message);
    }

    public void addResult(@NonNull ValidationResult result) {
        messages.addAll(result.messages);
    }

    @Override
    public String toString() {
        return Stream.of(messages).collect(Collectors.joining("\n"));
    }
}
