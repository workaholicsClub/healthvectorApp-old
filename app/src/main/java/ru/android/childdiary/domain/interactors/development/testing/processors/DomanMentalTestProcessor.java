package ru.android.childdiary.domain.interactors.development.testing.processors;

import android.support.annotation.Nullable;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.BaseTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.tests.DomanMentalTest;
import ru.android.childdiary.domain.interactors.development.testing.tests.core.Question;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DomanMentalTestProcessor extends BaseTestProcessor<DomanMentalTest> {
    public DomanMentalTestProcessor(@NonNull DomanMentalTest test) {
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
}
