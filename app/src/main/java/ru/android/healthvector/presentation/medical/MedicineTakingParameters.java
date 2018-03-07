package ru.android.healthvector.presentation.medical;

import org.joda.time.LocalTime;

import lombok.Builder;
import lombok.Value;
import ru.android.healthvector.domain.medical.data.MedicineTaking;

@Value
@Builder
public class MedicineTakingParameters {
    MedicineTaking defaultMedicineTaking;
    LocalTime startTime;
    LocalTime finishTime;
}
