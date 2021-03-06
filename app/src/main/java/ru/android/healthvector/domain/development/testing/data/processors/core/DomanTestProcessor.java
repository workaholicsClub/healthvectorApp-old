package ru.android.healthvector.domain.development.testing.data.processors.core;

import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.domain.development.testing.data.tests.core.DomanTest;
import ru.android.healthvector.domain.development.testing.data.tests.core.Question;
import ru.android.healthvector.utils.strings.TimeUtils;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class DomanTestProcessor<T extends DomanTest> extends BaseTestProcessor<T> {
    @Getter
    private final DomanTestParameter parameter;
    @Getter
    private final LocalDate birthDate;
    @Getter
    private final LocalDate date;

    private final List<Question> questions;
    private final List<Boolean> answers = new ArrayList<>();

    @Getter
    private final int initialStage;
    private int index, stage;
    private boolean stopped;
    @Nullable
    private Boolean lastAnswer;
    @Nullable
    private Integer result;
    @Nullable
    private LocalDate domanDate;

    protected DomanTestProcessor(@NonNull T test,
                                 @NonNull DomanTestParameter parameter,
                                 @NonNull LocalDate birthDate,
                                 @NonNull LocalDate date) {
        super(test);
        this.parameter = parameter;
        this.birthDate = birthDate;
        this.date = date;

        questions = Collections.unmodifiableList(test.getQuestions().get(parameter));
        for (int i = 0; i < questions.size(); ++i) {
            answers.add(null);
        }

        double months = TimeUtils.getMonths(birthDate, date);
        index = DomanTestProcessorUtils.getIndex(months);
        initialStage = index;
    }

    @Override
    public boolean isFinished() {
        return index < 0 || index >= questions.size() || stopped;
    }

    @Override
    public void goToNextQuestion() {
        Boolean answer = answers.get(index);
        if (answer == null) {
            throw new IllegalStateException("Don't skip questions!");
        }
        if (answer) {
            if (lastAnswer == null || lastAnswer) {
                // ответили Да, предыдущий ответ был Да ИЛИ не было ответа
                stage = index;
                ++index;
            } else {
                // ответили Да, предыдущий ответ был Нет
                stage = index;
                stopped = true;
            }
        } else {
            if (lastAnswer != null && lastAnswer) {
                // ответили Нет, предыдущий ответ был Да
                stage = index - 1;
                stopped = true;
            } else {
                // ответили Нет, предыдущий ответ был Нет ИЛИ не было ответа
                stage = index > 0 ? index - 1 : 0;
                --index;
            }
        }
        lastAnswer = answer;
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

    @Override
    public int getResult() {
        if (result != null) {
            return result;
        }
        return stage;
    }

    @Override
    public void setResult(@Nullable Integer result) {
        this.result = result;
    }

    @Nullable
    public LocalDate getDomanDate() {
        return domanDate;
    }

    public void setDomanDate(@Nullable LocalDate domanDate) {
        this.domanDate = domanDate;
    }

    public DomanTestParameter getDomanParameter() {
        return parameter;
    }

    public DomanResult getDomanResult() {
        return DomanResult.builder()
                .birthDate(birthDate)
                .date(date)
                .stage(getResult())
                .domanDate(domanDate)
                .build();
    }
}
