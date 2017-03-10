package ru.android.childdiary.domain.core.events;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SelectedDateChangedEvent {
    @NonNull
    LocalDate date;
}
