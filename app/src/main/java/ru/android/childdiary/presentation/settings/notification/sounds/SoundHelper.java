package ru.android.childdiary.presentation.settings.notification.sounds;

import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.android.childdiary.domain.calendar.data.core.SoundInfo;

public class SoundHelper {
    private final Context context;

    public SoundHelper(Context context) {
        this.context = context;
    }

    @SuppressWarnings("ConstantConditions")
    public List<SoundInfo> getAllAvailableSounds() {
        List<SoundInfo> sounds = new ArrayList<>();
        putSounds(RingtoneManager.TYPE_NOTIFICATION, sounds);
        putSounds(RingtoneManager.TYPE_RINGTONE, sounds);
        putSounds(RingtoneManager.TYPE_ALARM, sounds);
        putMusic(sounds);
        Collections.sort(sounds, (soundInfo1, soundInfo2) -> {
            int result = soundInfo1.getName().compareTo(soundInfo2.getName());
            return result == 0 ? soundInfo1.getPath().compareTo(soundInfo2.getPath()) : result;
        });
        sounds.add(0, SoundInfo.NULL);
        return sounds;
    }

    private void putSounds(int type, @NonNull List<SoundInfo> sounds) {
        RingtoneManager ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(type);
        Cursor cursor = ringtoneManager.getCursor();
        if (cursor == null) {
            return;
        }
        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
                String path = cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/"
                        + cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
                add(sounds, name, path);
            }
        } finally {
            cursor.close();
        }
    }

    private void putMusic(@NonNull List<SoundInfo> sounds) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media._ID},
                MediaStore.Audio.Media.IS_MUSIC + " != 0",
                null,
                MediaStore.Audio.Media.TITLE + " ASC");
        if (cursor == null) {
            return;
        }
        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String path = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/"
                        + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                add(sounds, name, path);
            }
        } finally {
            cursor.close();
        }
    }

    private void add(@NonNull List<SoundInfo> sounds, @Nullable String name, @Nullable String path) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(path)) {
            return;
        }
        SoundInfo soundInfo = SoundInfo.builder()
                .name(name)
                .path(path)
                .build();
        if (sounds.contains(soundInfo)) {
            return;
        }
        sounds.add(soundInfo);
    }
}
