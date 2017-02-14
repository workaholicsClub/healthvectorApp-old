package ru.android.childdiary.utils.log;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import lombok.val;
import ru.android.childdiary.BuildConfig;

public class LogSystem {
    public static void initLogger(Context appContext) {
        CrashReportSystem.init(appContext);

        val factories = new ArrayList<AbstractCustomAppenderFactory>();
        if (BuildConfig.DEBUG) {
            factories.add(new FileCustomAppenderFactory());
        }
        factories.add(new CrashReportSystemCustomAppenderFactory());
        if (BuildConfig.DEBUG) {
            factories.add(new LogcatCustomAppenderFactory());
        }

        val loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();

        val root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.TRACE);

        for (val factory : factories) {
            try {
                val appender = factory.getAppender(loggerContext, appContext);
                root.addAppender(appender);
            } catch (Exception e) {
                report(null, "failed to configure log " + factory, e);
            }
        }

        StatusPrinter.print(loggerContext);
    }

    public static void report(@Nullable Logger logger, @NonNull Throwable throwable) {
        report(logger, null, throwable);
    }

    public static void report(@Nullable Logger logger, @Nullable String msg, @NonNull Throwable throwable) {
        if (logger == null) {
            Log.e(LogConstants.APPLICATION_LOG_TAG, msg, throwable); // logcat appender
            CrashReportSystem.log(Log.ERROR, LogConstants.APPLICATION_LOG_TAG, toMessage(msg, throwable)); // crash report system appender
        } else {
            logger.error(msg, throwable);
        }

        CrashReportSystem.report(throwable);
    }

    private static String toMessage(@Nullable String msg, @NonNull Throwable throwable) {
        String stackTraceString = Log.getStackTraceString(throwable);
        msg = TextUtils.isEmpty(msg) ? stackTraceString : msg + "\n" + stackTraceString;
        return msg;
    }
}
