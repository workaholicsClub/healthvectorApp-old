package ru.android.childdiary.domain.interactors.development.testing.data.interpreters;

import android.content.Context;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.android.childdiary.domain.interactors.development.testing.data.interpreters.core.BaseTestInterpreter;
import ru.android.childdiary.domain.interactors.development.testing.data.processors.NewbornTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.NewbornTest;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NewbornTestInterpreter extends BaseTestInterpreter<NewbornTest, NewbornTestProcessor> {
    public NewbornTestInterpreter(Context context, NewbornTestProcessor testProcessor) {
        super(context, testProcessor);
    }

    @Override
    public String interpret() {
        int count = testProcessor.getResult();
        return count == 0 ? test.getResultGood() : test.getResultBad();
    }

    @Override
    public String interpretShort() {
        int count = testProcessor.getResult();
        return count == 0 ? test.getShortResultGood() : test.getShortResultBad();
    }
}
