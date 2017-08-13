package ru.android.childdiary.domain.development.testing.data.interpreters;

import android.content.Context;
import android.support.annotation.StringRes;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.R;
import ru.android.childdiary.domain.development.testing.data.interpreters.core.BaseTestInterpreter;
import ru.android.childdiary.domain.development.testing.data.processors.AutismTestProcessor;
import ru.android.childdiary.domain.development.testing.data.tests.AutismTest;
import ru.android.childdiary.utils.ui.FormatTextHelper;

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
            return getAutismFinishText(R.string.test_autism_finish_text_low, count);
        } else if (count <= 7) {
            return getAutismFinishText(R.string.test_autism_finish_text_medium, count);
        } else {
            return getAutismFinishText(R.string.test_autism_finish_text_high, count);
        }
    }

    @Override
    public String interpretShort() {
        int count = testProcessor.getResult();
        if (count <= 2) {
            return context.getString(R.string.test_autism_low);
        } else if (count <= 7) {
            return context.getString(R.string.test_autism_medium);
        } else {
            return context.getString(R.string.test_autism_high);
        }
    }

    private String getAutismFinishText(@StringRes int stringId, int count) {
        String countStr = context.getResources().getQuantityString(R.plurals.numberOfPoints, count, count);
        return FormatTextHelper.getParagraphWithCenterAlignment(countStr)
                + FormatTextHelper.getParagraphWithJustifyAlignment(context, stringId)
                + FormatTextHelper.getParagraphWithJustifyAlignment(context, R.string.test_autism_finish_text);
    }
}
