package ru.android.childdiary.presentation.medical;

import org.joda.time.LocalTime;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.domain.interactors.medical.data.MedicineTaking;

@Value
@Builder
public class MedicineTakingParameters {
    MedicineTaking defaultMedicineTaking;
    LocalTime startTime;
    LocalTime finishTime;
}
