package ru.android.childdiary.presentation.settings.notification.ringtones;

import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

public final class RingtoneUtils {
    static void getRingTone(Context context,
                            @NonNull HashMap<String, Uri> ringTonesList) {
        getTone(context, RingtoneManager.TYPE_RINGTONE, ringTonesList);
    }

    static void getNotificationTones(Context context,
                                     @NonNull HashMap<String, Uri> ringTonesList) {
        getTone(context, RingtoneManager.TYPE_NOTIFICATION, ringTonesList);
    }

    static void getAlarmTones(Context context,
                              @NonNull HashMap<String, Uri> ringTonesList) {
        getTone(context, RingtoneManager.TYPE_ALARM, ringTonesList);
    }

    private static void getTone(Context context,
                                int type,
                                @NonNull HashMap<String, Uri> ringTonesList) {
        RingtoneManager mRingtoneMgr = new RingtoneManager(context);

        mRingtoneMgr.setType(type);
        Cursor ringsCursor = mRingtoneMgr.getCursor();

        while (ringsCursor.moveToNext()) {
            ringTonesList.put(ringsCursor.getString(RingtoneManager.TITLE_COLUMN_INDEX),
                    Uri.parse(ringsCursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/"
                            + ringsCursor.getString(RingtoneManager.ID_COLUMN_INDEX)));
        }
        ringsCursor.close();
    }

    static void getMusic(@NonNull Context context,
                         @NonNull HashMap<String, Uri> ringTonesList) {
        Cursor mediaCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media._ID},
                MediaStore.Audio.Media.IS_MUSIC + " != 0",
                null,
                MediaStore.Audio.Media.TITLE + " ASC");

        if (mediaCursor != null) {
            while (mediaCursor.moveToNext()) {
                ringTonesList.put(mediaCursor.getString(mediaCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                        Uri.parse(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/"
                                + mediaCursor.getString(mediaCursor.getColumnIndex(MediaStore.Audio.Media._ID))));
            }
            mediaCursor.close();
        }
    }

    @Nullable
    public static String getRingtoneName(@NonNull Context context,
                                         @NonNull Uri uri) {
        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        if (ringtone != null) {
            return ringtone.getTitle(context);
        } else {
            Cursor cur = context
                    .getContentResolver()
                    .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            new String[]{MediaStore.Audio.Media.TITLE},
                            MediaStore.Audio.Media._ID + " = ?",
                            new String[]{uri.getLastPathSegment()},
                            null);

            String title = null;
            if (cur != null) {
                title = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE));
                cur.close();
            }
            return title;
        }
    }
}
