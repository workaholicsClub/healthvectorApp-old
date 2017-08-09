package ru.android.childdiary.domain.interactors.development.testing.data.interpreters.core;

import android.content.Context;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.domain.interactors.development.testing.data.processors.core.DomanTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.core.DomanTest;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class DomanTestInterpreter<T extends DomanTest, TP extends DomanTestProcessor<T>>
        extends BaseTestInterpreter<T, TP> {
    protected DomanTestInterpreter(Context context,
                                   @NonNull TP testProcessor) {
        super(context, testProcessor);
    }

    @Override
    public String interpret() {
        int stage = testProcessor.getResult();
        return String.format(test.getResultTextFormat(),
                test.getStageTitle(stage),
                test.getStageDescription(stage),
                test.getStageType(testProcessor.getInitialStage(), stage, testProcessor.getDomanDate()));
    }

    @Override
    public String interpretShort() {
        int stage = testProcessor.getResult();
        return test.getStageTitle(stage);
    }
}
