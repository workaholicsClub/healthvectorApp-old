package ru.android.childdiary.domain.interactors.development.testing.data.interpreters;

import android.content.Context;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.domain.interactors.development.testing.data.interpreters.core.BaseTestInterpreter;
import ru.android.childdiary.domain.interactors.development.testing.data.processors.AutismTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.AutismTest;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AutismTestInterpreter extends BaseTestInterpreter<AutismTest, AutismTestProcessor> {
    public AutismTestInterpreter(Context context,
                                 @NonNull AutismTestProcessor testProcessor) {
        super(context, testProcessor);
    }

    @Override
    public String interpret() {
        int count = testProcessor.getResult();
        if (count <= 2) {
            return test.getFinishTextLow();
        } else if (count <= 7) {
            return test.getFinishTextMedium();
        } else {
            return test.getFinishTextHigh();
        }
    }

    @Override
    public String interpretShort() {
        int count = testProcessor.getResult();
        if (count <= 2) {
            return test.getShortTextLow();
        } else if (count <= 7) {
            return test.getShortTextMedium();
        } else {
            return test.getShortTextHigh();
        }
    }
}
