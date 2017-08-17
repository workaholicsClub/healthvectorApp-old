package ru.android.childdiary.presentation.settings.notification.sounds.adapters;

import android.support.annotation.NonNull;

import ru.android.childdiary.domain.calendar.data.core.SoundInfo;

public interface SoundSelectedListener {
    void onSoundSelected(int position, @NonNull SoundInfo soundInfo);
}
