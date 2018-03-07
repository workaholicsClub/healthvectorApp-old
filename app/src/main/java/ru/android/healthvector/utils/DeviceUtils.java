package ru.android.healthvector.utils;

import android.os.Environment;

public class DeviceUtils {
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
