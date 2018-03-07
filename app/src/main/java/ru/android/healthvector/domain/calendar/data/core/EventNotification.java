package ru.android.healthvector.domain.calendar.data.core;

import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.Serializable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.android.healthvector.data.types.EventType;

@Value
@Builder
public class EventNotification implements Serializable {
    @NonNull
    EventType eventType;
    boolean dontNotify;
    @Nullable
    Integer minutes;
    @Nullable
    SoundInfo soundInfo;
    boolean vibration;

    @Nullable
    public Integer getNotifyTime() {
        return dontNotify ? null : minutes;
    }

    @Nullable
    public Uri getSoundUri() {
        return soundInfo == null ? null : soundInfo.getSoundUri();
    }
}
