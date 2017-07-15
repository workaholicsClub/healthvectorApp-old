package ru.android.childdiary.domain.interactors.development.testing.processors.core;

import android.support.annotation.Nullable;

import ru.android.childdiary.domain.interactors.development.testing.tests.core.Question;

public interface TestProcessor {
    boolean isFinished();

    @Nullable
    Boolean goToNextQuestion();

    @Nullable
    Question getCurrentQuestion();

    @Nullable
    String getResultText();

    int getResultNumber();
}
