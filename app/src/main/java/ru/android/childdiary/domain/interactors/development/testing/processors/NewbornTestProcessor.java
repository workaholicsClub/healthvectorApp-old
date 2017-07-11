package ru.android.childdiary.domain.interactors.development.testing.processors;

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
}
