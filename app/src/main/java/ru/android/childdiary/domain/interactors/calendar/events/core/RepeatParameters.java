package ru.android.childdiary.domain.interactors.calendar.events.core;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.entities.calendar.events.core.LinearGroups;

@Value
@Builder
public class RepeatParameters {
    Long id;

    DateTime dateTimeFrom;

    Integer periodicityInMinutes;

    Integer lengthInMinutes;

    LinearGroups linearGroups;
}
