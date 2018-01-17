package ru.android.childdiary.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import ru.android.childdiary.presentation.splash.SplashActivity;

public class AppRestartUtils {
    public static void scheduleAppStartAndExit(Context context) {
        Intent intent = SplashActivity.getIntent(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            return;
        }
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 500, pendingIntent);

        // the better way to close all database connections
        // перед вызовом этого метода все активити уже закрыты
        System.exit(0);
    }
}
