package ru.android.childdiary.domain.interactors.development.testing.data.processors;

import android.support.annotation.Nullable;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.domain.interactors.development.testing.data.processors.core.SimpleTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.NewbornTest;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NewbornTestProcessor extends SimpleTestProcessor<NewbornTest> {
    @Nullable
    private Integer result;

    public NewbornTestProcessor(@NonNull NewbornTest test) {
        super(test);
    }

    @Override
    public int getResult() {
        if (result != null) {
            return result;
        }
        result = 0;
        for (int i = 0; i < answers.size(); ++i) {
            Boolean answer = answers.get(i);
            if (answer == null) {
                continue;
            }
            boolean yes = answers.get(i);
            if (yes) {
                result = 1;
                break;
            }
        }
        return result;
    }

    @Override
    public void setResult(@Nullable Integer result) {
        this.result = result;
    }
}
