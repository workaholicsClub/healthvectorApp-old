package ru.android.childdiary.utils.log;

import android.content.Context;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

class CrashReportSystem {
    public static void init(Context appContext) {
        // Crashlytics or Firebase initialization
        Fabric.with(appContext, new Crashlytics());
    }

    public static void report(Throwable throwable) {
        // report unexpected exception to Crashlytics or Firebase
        Crashlytics.logException(throwable);
    }

    public static void log(String msg) {
        // log to Crashlytics or Firebase
        Crashlytics.log(msg);
    }
}
