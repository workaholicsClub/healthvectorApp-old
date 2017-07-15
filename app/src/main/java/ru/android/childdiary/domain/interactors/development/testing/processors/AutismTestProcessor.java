package ru.android.childdiary.domain.interactors.development.testing.processors;

import android.support.annotation.Nullable;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.SimpleTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.tests.AutismTest;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AutismTestProcessor extends SimpleTestProcessor<AutismTest> {
    private Integer result;

    public AutismTestProcessor(@NonNull AutismTest test) {
        super(test);
    }

    @Nullable
    @Override
    public String getResultText() {
        int count = getResultNumber();
        if (count <= 2) {
            return test.getFinishTextLow();
        } else if (count <= 7) {
            return test.getFinishTextMedium();
        } else {
            return test.getFinishTextHigh();
        }
    }

    @Override
    public int getResultNumber() {
        int count = 0;
        for (int i = 0; i < answers.size(); ++i) {
            Boolean answer = answers.get(i);
            if (answer == null) {
                continue;
            }
            boolean yes = answers.get(i);
            boolean no = !yes;
            if (i == 1 || i == 5 || i == 11) {
                if (yes) {
                    ++count;
                }
            } else {
                if (no) {
                    ++count;
                }
            }
        }
        return count;
    }
}
