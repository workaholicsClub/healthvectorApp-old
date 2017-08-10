package ru.android.childdiary.domain.interactors.development.testing.data.interpreters;

import android.content.Context;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.android.childdiary.R;
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
        return count == 0
                ? context.getString(R.string.test_newborn_result_good)
                : context.getString(R.string.test_newborn_result_bad);
    }

    @Override
    public String interpretShort() {
        int count = testProcessor.getResult();
        return count == 0
                ? context.getString(R.string.test_newborn_short_result_good)
                : context.getString(R.string.test_newborn_short_result_bad);
    }
}
