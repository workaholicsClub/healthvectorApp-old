package ru.android.healthvector.domain.development.testing.data.interpreters;

import android.content.Context;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.development.testing.data.interpreters.core.BaseTestInterpreter;
import ru.android.healthvector.domain.development.testing.data.processors.NewbornTestProcessor;
import ru.android.healthvector.domain.development.testing.data.tests.NewbornTest;
import ru.android.healthvector.utils.ui.FormatTextHelper;

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
                ? FormatTextHelper.getParagraphWithCenterAlignment(context.getString(R.string.test_newborn_result_good))
                : FormatTextHelper.getParagraphsWithCenterAlignment(context.getResources().getStringArray(R.array.test_newborn_result_bad_paragraphs));
    }

    @Override
    public String interpretShort() {
        int count = testProcessor.getResult();
        return count == 0
                ? context.getString(R.string.test_newborn_short_result_good)
                : context.getString(R.string.test_newborn_short_result_bad);
    }
}
