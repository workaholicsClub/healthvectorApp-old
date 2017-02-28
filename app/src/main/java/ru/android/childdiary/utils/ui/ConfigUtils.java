package ru.android.childdiary.utils.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;

import ru.android.childdiary.BuildConfig;

public class ConfigUtils {
    public static void setupOrientation(Activity activity) {
        if (!BuildConfig.DEBUG) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
