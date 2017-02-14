package ru.android.childdiary.utils.log;

import android.content.Context;
import android.util.Log;

class CrashReportSystem {
    public static void init(Context appContext) {
        // TODO: Crashlytics or Firebase initialization
    }

    public static void report(Throwable throwable) {
        // TODO: report unexpected exception to Crashlytics or Firebase
    }

    public static void log(int priority, String tag, String msg) {
        // TODO: log to Crashlytics or Firebase
    }
}
