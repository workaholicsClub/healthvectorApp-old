package ru.android.childdiary.data.entities.calendar.events.core;

import org.joda.time.LocalTime;

import java.util.List;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class LinearGroups {
    @NonNull
    List<LocalTime> times;
}
