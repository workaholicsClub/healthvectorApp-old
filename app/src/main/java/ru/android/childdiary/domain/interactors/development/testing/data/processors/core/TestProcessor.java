package ru.android.childdiary.domain.interactors.development.testing.data.processors.core;

import android.support.annotation.Nullable;

import ru.android.childdiary.domain.interactors.development.testing.data.tests.core.Question;

public interface TestProcessor {
    boolean isFinished();

    @Nullable
    Boolean goToNextQuestion();

    @Nullable
    Question getCurrentQuestion();

    int getResult();

    void setResult(@Nullable Integer result);
}
