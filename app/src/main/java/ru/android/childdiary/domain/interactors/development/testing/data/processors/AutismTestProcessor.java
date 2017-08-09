package ru.android.childdiary.domain.interactors.development.testing.data.processors;

import android.support.annotation.Nullable;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.domain.interactors.development.testing.data.processors.core.SimpleTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.AutismTest;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AutismTestProcessor extends SimpleTestProcessor<AutismTest> {
    @Nullable
    private Integer result;

    public AutismTestProcessor(@NonNull AutismTest test) {
        super(test);
    }

    @Override
    public int getResult() {
        if (result != null) {
            return result;
        }
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
        result = count;
        return count;
    }

    @Override
    public void setResult(@Nullable Integer result) {
        this.result = result;
    }
}
