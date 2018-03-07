package ru.android.healthvector.domain.development.testing.data.processors.core;

import android.support.annotation.Nullable;

import ru.android.healthvector.domain.development.testing.data.tests.core.Question;

public interface TestProcessor {
    boolean isFinished();

    void goToNextQuestion();

    @Nullable
    Question getCurrentQuestion();

    int getResult();

    void setResult(@Nullable Integer result);
}
