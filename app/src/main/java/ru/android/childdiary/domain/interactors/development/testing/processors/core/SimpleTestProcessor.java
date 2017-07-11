package ru.android.childdiary.domain.interactors.development.testing.processors.core;

import android.support.annotation.Nullable;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Question;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.SimpleTest;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class SimpleTestProcessor<T extends SimpleTest> extends BaseTestProcessor<T> {
    private final List<Question> questions;
    private int index;

    public SimpleTestProcessor(@NonNull T test) {
        super(test);
        questions = test.getQuestions();
    }

    @Override
    public boolean isFinished() {
        return index < 0 || index >= questions.size();
    }

    @Override
    public void goToNextQuestion() {
        ++index;
    }

    @Nullable
    @Override
    public Question getCurrentQuestion() {
        return isFinished() ? null : questions.get(index);
    }
}
