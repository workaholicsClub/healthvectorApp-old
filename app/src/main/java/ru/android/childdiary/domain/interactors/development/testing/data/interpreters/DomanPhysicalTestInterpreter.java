package ru.android.childdiary.domain.interactors.development.testing.data.interpreters;

import android.content.Context;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.domain.interactors.development.testing.data.interpreters.core.DomanTestInterpreter;
import ru.android.childdiary.domain.interactors.development.testing.data.processors.DomanPhysicalTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.DomanPhysicalTest;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DomanPhysicalTestInterpreter extends DomanTestInterpreter<DomanPhysicalTest, DomanPhysicalTestProcessor> {
    public DomanPhysicalTestInterpreter(Context context, @NonNull DomanPhysicalTestProcessor testProcessor) {
        super(context, testProcessor);
    }
}
