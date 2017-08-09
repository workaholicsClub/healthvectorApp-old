package ru.android.childdiary.domain.interactors.development.testing.data.interpreters;

import android.content.Context;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.childdiary.domain.interactors.development.testing.data.interpreters.core.DomanTestInterpreter;
import ru.android.childdiary.domain.interactors.development.testing.data.processors.DomanMentalTestProcessor;
import ru.android.childdiary.domain.interactors.development.testing.data.tests.DomanMentalTest;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DomanMentalTestInterpreter extends DomanTestInterpreter<DomanMentalTest, DomanMentalTestProcessor> {
    public DomanMentalTestInterpreter(Context context, @NonNull DomanMentalTestProcessor testProcessor) {
        super(context, testProcessor);
    }
}
