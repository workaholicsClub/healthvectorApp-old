package ru.android.childdiary.domain.calendar.data.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.types.EventType;

@Value
@Builder
public class EventNotification {
    @Nullable
    Integer minutes;
    @NonNull
    EventType eventType;
}
