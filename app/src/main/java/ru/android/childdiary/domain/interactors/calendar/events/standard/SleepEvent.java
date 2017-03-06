package ru.android.childdiary.domain.interactors.calendar.events.standard;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.entities.calendar.events.MasterEventData;

@Value
@Builder
public class SleepEvent {
    Long id;

    MasterEventData masterEvent;

    DateTime awakeTime;
}
