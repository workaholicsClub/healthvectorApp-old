package ru.android.childdiary.domain.calendar.data.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;
import ru.android.childdiary.data.types.EventType;

@Value
@Builder
public class EventNotification implements Serializable {
    @NonNull
    EventType eventType;
    boolean dontNotify;
    @Nullable
    Integer minutes;
    // TODO Melody
    boolean vibration;

    @Nullable
    public Integer getNotifyTime() {
        return dontNotify ? null : minutes;
    }
}
