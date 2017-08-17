package ru.android.childdiary.domain.calendar.data.core;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SoundInfo implements Serializable {
    public static final SoundInfo NULL = SoundInfo.builder().build();

    @Nullable
    String name;
    @Nullable
    String path;

    @Nullable
    public Uri getSoundUri() {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return Uri.parse(path);
    }
}
