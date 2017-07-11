package ru.android.childdiary.domain.interactors.development.testing.processors;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.domain.interactors.development.testing.processors.core.SimpleTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.tests.AutismTest;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AutismTestProcessor extends SimpleTestProcessor<AutismTest> {
    public AutismTestProcessor(@NonNull AutismTest test) {
        super(test);
    }
}
