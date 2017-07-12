package ru.android.childdiary.domain.interactors.development.testing.processors.core;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.data.types.DomanTestParameter;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.DomanTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Question;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class DomanTestProcessor<T extends DomanTest> extends BaseTestProcessor<T> {
    private final LocalDate date;
    private final DomanTestParameter parameter;
    protected final List<Boolean> answers = new ArrayList<>();
    private final List<Question> questions;
    private int index, stage;
    private boolean stopped;
    @Nullable
    private Boolean lastAnswer;

    public DomanTestProcessor(@NonNull T test,
                              @NonNull LocalDate date,
                              @NonNull DomanTestParameter parameter) {
        super(test);
        this.date = date;
        this.parameter = parameter;
        questions = Collections.unmodifiableList(test.getQuestions().get(parameter));
        for (int i = 0; i < questions.size(); ++i) {
            answers.add(null);
        }
        // TODO определить индекс по возрасту
    }

    @Override
    public boolean isFinished() {
        return index < 0 || index >= questions.size() || stopped;
    }

    @Override
    public Boolean goToNextQuestion() {
        Boolean answer = answers.get(index);
        if (answer == null) {
            throw new IllegalStateException("Don't skip questions!");
        }
        Boolean forward = null;
        if (answer) {
            if (lastAnswer == null || lastAnswer) {
                // ответили Да, предыдущий ответ был Да ИЛИ не было ответа, фиксируем стадию
                stage = index;
                ++index;
                forward = true;
            } else {
                // ответили Да, предыдущий ответ был Нет, не меняем стадию
                stopped = true;
            }
        } else {
            if (lastAnswer != null && lastAnswer) {
                // ответили Нет, предыдущий ответ был Да, не меняем стадию
                stopped = true;
            } else {
                // ответили Нет, предыдущий ответ был Нет ИЛИ не было ответа, фиксируем стадию
                stage = index;
                --index;
                forward = false;
            }
        }
        lastAnswer = answer;
        return forward;
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

    @Nullable
    @Override
    public String getResultText() {
        int stage = this.stage + 1;
        return String.format(test.getResultTextFormat(), stage);
    }
}
