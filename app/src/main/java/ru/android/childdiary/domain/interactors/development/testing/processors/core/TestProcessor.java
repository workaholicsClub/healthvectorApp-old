package ru.android.childdiary.domain.interactors.development.testing.processors.core;

import android.support.annotation.Nullable;

import ru.android.childdiary.domain.interactors.development.testing.tests.core.Question;

public interface TestProcessor {
    boolean isFinished();

    void goToNextQuestion();

    @Nullable
    Question getCurrentQuestion();
}
