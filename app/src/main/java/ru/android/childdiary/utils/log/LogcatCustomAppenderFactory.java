package ru.android.childdiary.utils.log;

import android.content.Context;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import lombok.val;

class LogcatCustomAppenderFactory extends AbstractCustomAppenderFactory {
    private static final String TAG_PATTERN = LogConstants.APPLICATION_LOG_TAG + " %logger{0}";
    private static final String LOG_PATTERN = "%message%n";

    @Override
    public Appender<ILoggingEvent> getAppender(LoggerContext loggerContext, Context appContext) {
        val logcatAppender = new LogcatAppender();
        logcatAppender.setContext(loggerContext);

        logcatAppender.setEncoder(getPatternLayoutEncoder(loggerContext, LOG_PATTERN));
        logcatAppender.setTagEncoder(getPatternLayoutEncoder(loggerContext, TAG_PATTERN));

        logcatAppender.start();
        return logcatAppender;
    }
}
