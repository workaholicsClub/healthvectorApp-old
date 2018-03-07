package ru.android.healthvector.presentation.settings.notification.sounds.adapters;

import android.support.annotation.NonNull;

import ru.android.healthvector.domain.calendar.data.core.SoundInfo;

public interface SoundSelectedListener {
    void onSoundSelected(int position, @NonNull SoundInfo soundInfo);
}
