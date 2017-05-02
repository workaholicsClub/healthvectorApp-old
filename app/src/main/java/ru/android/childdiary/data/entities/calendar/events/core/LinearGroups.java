package ru.android.childdiary.data.entities.calendar.events.core;

import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class LinearGroups implements Serializable {
    @NonNull
    ArrayList<LocalTime> times;
}
