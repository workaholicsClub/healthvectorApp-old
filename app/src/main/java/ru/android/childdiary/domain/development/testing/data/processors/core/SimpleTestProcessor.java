package ru.android.childdiary.domain.development.testing.data.processors.core;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.domain.development.testing.data.tests.core.Question;
import ru.android.childdiary.domain.development.testing.data.tests.core.SimpleTest;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class SimpleTestProcessor<T extends SimpleTest> extends BaseTestProcessor<T> {
    protected final List<Boolean> answers = new ArrayList<>();
    private final List<Question> questions;
    private int index;

    protected SimpleTestProcessor(@NonNull T test) {
        super(test);
        questions = Collections.unmodifiableList(test.getQuestions());
        for (int i = 0; i < questions.size(); ++i) {
            answers.add(null);
        }
    }

    @Override
    public boolean isFinished() {
        return index < 0 || index >= questions.size();
    }

    @Override
    public Boolean goToNextQuestion() {
        ++index;
        return true;
    }

    @Nullable
    @Override
    public Question getCurrentQuestion() {
        return isFinished() ? null : questions.get(index);
    }

    @Override
    public void answer(boolean value) {
        if (!isFinished()) {
            answers.set(index, value);
        }
    }
}
