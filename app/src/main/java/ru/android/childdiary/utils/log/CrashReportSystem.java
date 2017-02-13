package ru.android.childdiary.utils.log;

import android.content.Context;
import android.util.Log;

class CrashReportSystem {
    /**
     * Если система отчетов об ошибках дублирует сообщение в logcat, возвращает {@code true}.
     * Иначе (необходим вывод в logcat), возвращает {@code false}.
     * Пока система отчетов об ошибках не интегрирована, возвращает {@code false}.
     *
     * @return {@code true}, если необходимо писать в logcat
     */
    public static boolean printsToLogcat() {
        return false;
    }

    public static void init(Context appContext) {
        // TODO: Crashlytics or Firebase initialization
    }

    public static void report(Throwable throwable) {
        // TODO: report unexpected exception to Crashlytics or Firebase
    }

    public static void log(int priority, String tag, String msg) {
        // TODO: log to Crashlytics or Firebase
        // TODO: удалить после тестирования
        logcat(priority, tag, msg);
    }

    // TODO: удалить после тестирования
    private static void logcat(int level, String tag, String msg) {
        switch (level) {
            case Log.ASSERT:
                Log.wtf(tag, msg);
                break;
            case Log.ERROR:
                Log.e(tag, msg);
                break;
            case Log.WARN:
                Log.w(tag, msg);
                break;
            case Log.INFO:
                Log.i(tag, msg);
                break;
            case Log.DEBUG:
                Log.d(tag, msg);
                break;
            case Log.VERBOSE:
                Log.v(tag, msg);
                break;
            default:
                Log.println(level, tag, msg);
                break;
        }
    }
}
