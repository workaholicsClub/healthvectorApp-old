package ru.android.childdiary.utils.log;

import android.content.Context;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import lombok.val;

class CrashReportSystemCustomAppenderFactory extends AbstractCustomAppenderFactory {
    private static final String TAG_PATTERN = LogConstants.APPLICATION_LOG_TAG + " %logger{0}";
    private static final String LOG_FILE_PATTERN = "[%thread] %message%n";

    @Override
    public Appender<ILoggingEvent> getAppender(LoggerContext loggerContext, Context appContext) {
        val crashlyticsAppender = new CrashReportSystemAppender();
        crashlyticsAppender.setContext(loggerContext);

        crashlyticsAppender.setEncoder(getPatternLayoutEncoder(loggerContext, LOG_FILE_PATTERN));
        crashlyticsAppender.setTagEncoder(getPatternLayoutEncoder(loggerContext, TAG_PATTERN));

        crashlyticsAppender.start();
        return crashlyticsAppender;
    }
}
