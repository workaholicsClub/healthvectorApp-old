package ru.android.childdiary.domain.interactors.calendar.events.core;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.entities.calendar.events.core.LinearGroups;

@Value
@Builder(toBuilder = true)
public class RepeatParameters implements Serializable {
    Long id;

    LinearGroups linearGroups;

    Integer periodicityInMinutes;

    Integer lengthInMinutes;
}
