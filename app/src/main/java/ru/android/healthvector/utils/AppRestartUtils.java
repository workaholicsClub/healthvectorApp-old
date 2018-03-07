package ru.android.healthvector.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.android.healthvector.presentation.splash.SplashActivity;

public class AppRestartUtils {
    private static final Logger logger = LoggerFactory.getLogger(AppRestartUtils.class);

    public static void scheduleAppStartAndExit(Context context) {
        Intent intent = SplashActivity.getIntent(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            logger.error("alarm manager is null");
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 500, pendingIntent);
        }

        logger.debug("do exit from app");

        // the better way to close all database connections
        // перед вызовом этого метода все активити уже закрыты
        System.exit(0);
    }
}
