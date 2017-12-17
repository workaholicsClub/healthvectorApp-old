package ru.android.childdiary.utils.log;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;
import ru.android.childdiary.BuildConfig;

class CrashReportSystem {
    public static void init(Context appContext) {
        // Crashlytics or Firebase initialization
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(!BuildConfig.ENABLE_CRASHLYTICS).build())
                .build();
        Fabric.with(appContext, crashlyticsKit);
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
