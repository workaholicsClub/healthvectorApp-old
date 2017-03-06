package ru.android.childdiary.domain.interactors.calendar.events.standard;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.entities.calendar.events.MasterEventData;
import ru.android.childdiary.data.types.Breast;

@Value
@Builder
public class PumpEvent {
    Long id;

    MasterEventData masterEvent;

    Breast breast;

    Integer durationInMinutes;
}
