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
import ru.android.childdiary.utils.strings.TimeUtils;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class DomanTestProcessor<T extends DomanTest> extends BaseTestProcessor<T> {
    private final DomanTestParameter parameter;
    private final LocalDate date;
    private final double months;
    private final List<Question> questions;
    private final List<Boolean> answers = new ArrayList<>();
    private final int initialStage;
    @Nullable
    private Integer result;
    private int index, stage;
    private boolean stopped;
    @Nullable
    private Boolean lastAnswer;

    private DomanTestProcessor(@NonNull T test,
                               @NonNull DomanTestParameter parameter,
                               @NonNull LocalDate date,
                               double months) {
        super(test);
        this.parameter = parameter;
        this.date = date;
        this.months = months;
        questions = Collections.unmodifiableList(test.getQuestions().get(parameter));
        for (int i = 0; i < questions.size(); ++i) {
            answers.add(null);
        }
        index = DomanTestProcessorHelper.getIndex(months);
        initialStage = index;
    }

    public DomanTestProcessor(@NonNull T test,
                              @NonNull DomanTestParameter parameter,
                              @NonNull TimeUtils.Age age,
                              @NonNull LocalDate date) {
        this(test, parameter, date, DomanTestProcessorHelper.getMonths(age));
    }

    public DomanTestProcessor(@NonNull T test,
                              @NonNull DomanTestParameter parameter,
                              @NonNull LocalDate birthDate,
                              @NonNull LocalDate date) {
        this(test, parameter, date, DomanTestProcessorHelper.getMonths(birthDate, date));
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
                // ответили Да, предыдущий ответ был Да ИЛИ не было ответа
                stage = index;
                ++index;
                forward = true;
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

    @Override
    public String interpretResult() {
        int stage = getResult();
        return String.format(test.getResultTextFormat(),
                stage + 1,
                test.getStageDescription(stage),
                test.getStageType(initialStage, stage));
    }

    public DomanTestParameter getDomanParameter() {
        return parameter;
    }

    public DomanResult getDomanResult() {
        return DomanResult.builder()
                .date(date)
                .months(months)
                .stage(getResult())
                .build();
    }
}
