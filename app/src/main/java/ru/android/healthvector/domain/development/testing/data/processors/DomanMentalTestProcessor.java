package ru.android.healthvector.domain.development.testing.data.processors;

import org.joda.time.LocalDate;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.android.healthvector.data.types.DomanTestParameter;
import ru.android.healthvector.domain.development.testing.data.processors.core.DomanTestProcessor;
import ru.android.healthvector.domain.development.testing.data.tests.DomanMentalTest;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DomanMentalTestProcessor extends DomanTestProcessor<DomanMentalTest> {
    public DomanMentalTestProcessor(@NonNull DomanMentalTest test,
                                    @NonNull DomanTestParameter parameter,
                                    @NonNull LocalDate birthDate,
                                    @NonNull LocalDate date) {
        super(test, parameter, birthDate, date);
    }
}
