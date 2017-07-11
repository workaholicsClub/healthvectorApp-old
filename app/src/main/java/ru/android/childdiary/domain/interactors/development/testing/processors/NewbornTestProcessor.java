package ru.android.childdiary.domain.interactors.development.testing.processors;

import android.support.annotation.Nullable;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.SimpleTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.tests.NewbornTest;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NewbornTestProcessor extends SimpleTestProcessor<NewbornTest> {
    public NewbornTestProcessor(@NonNull NewbornTest test) {
        super(test);
    }

    @Nullable
    @Override
    public String getResultText() {
        for (int i = 0; i < answers.size(); ++i) {
            Boolean answer = answers.get(i);
            if (answer == null) {
                continue;
            }
            boolean yes = answers.get(i);
            if (yes) {
                return test.getResultBad();
            }
        }
        return test.getResultGood();
    }
}
