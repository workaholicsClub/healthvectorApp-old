package ru.android.childdiary.domain.interactors.development.testing.processors;

import android.support.annotation.Nullable;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.BaseTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.tests.DomanPhysicalTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Question;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DomanPhysicalTestProcessor extends BaseTestProcessor<DomanPhysicalTest> {
    public DomanPhysicalTestProcessor(@NonNull DomanPhysicalTest test) {
        super(test);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void goToNextQuestion() {

    }

    @Nullable
    @Override
    public Question getCurrentQuestion() {
        return null;
    }

    @Override
    public void answer(boolean value) {

    }

    @Nullable
    @Override
    public String getResultText() {
        return null;
    }
}
