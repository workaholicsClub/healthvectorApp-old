package ru.android.healthvector.utils;

import android.media.RingtoneManager;
import android.net.Uri;

public class RingtoneUtils {
    public static Uri getDefaultNotificationUri() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }
}
