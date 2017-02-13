package ru.android.childdiary.utils.log;

import android.content.Context;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import lombok.val;

abstract class AbstractCustomAppenderFactory {
    public abstract Appender<ILoggingEvent> getAppender(LoggerContext loggerContext, Context appContext);

    protected PatternLayoutEncoder getPatternLayoutEncoder(LoggerContext loggerContext, String logFilePattern) {
        val encoder = new PatternLayoutEncoder();

        encoder.setPattern(logFilePattern);

        encoder.setContext(loggerContext);
        encoder.start();
        return encoder;
    }
}
