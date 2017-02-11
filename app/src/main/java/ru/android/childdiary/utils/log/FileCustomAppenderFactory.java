package ru.android.childdiary.utils.log;

import android.content.Context;

import java.io.File;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;
import lombok.val;

class FileCustomAppenderFactory extends AbstractCustomAppenderFactory {
    private static final int MAX_LOG_FILE_COUNT = 2;
    private static final String MAX_LOG_SIZE = "1MB";
    private static final String LOG_FILE_PATTERN = "%date [%thread] %level %logger{0} %message%n";
    private static final String LOG_FILE_NAME = File.separator + "log.txt";
    private static final String LOG_ROTATE_PATTERN = File.separator + "log.%i.txt";

    @Override
    public Appender<ILoggingEvent> getAppender(LoggerContext loggerContext, Context appContext) {
        val filesDir = appContext.getFilesDir().getAbsolutePath();
        return getAppender(loggerContext, filesDir);
    }

    private Appender<ILoggingEvent> getAppender(LoggerContext loggerContext, String filesDir) {
        val fileName = filesDir + LOG_FILE_NAME;
        val fineNamePattern = filesDir + LOG_ROTATE_PATTERN;

        val rollingFileAppender = new RollingFileAppender<ILoggingEvent>();
        rollingFileAppender.setAppend(true);
        rollingFileAppender.setContext(loggerContext);

        rollingFileAppender.setFile(fileName);

        rollingFileAppender.setRollingPolicy(getRollingPolicy(loggerContext, rollingFileAppender, fineNamePattern));
        rollingFileAppender.setTriggeringPolicy(getTriggeringPolicy(loggerContext));

        rollingFileAppender.setEncoder(getPatternLayoutEncoder(loggerContext, LOG_FILE_PATTERN));

        rollingFileAppender.start();
        return rollingFileAppender;
    }

    private RollingPolicy getRollingPolicy(
            LoggerContext loggerContext,
            RollingFileAppender rollingFileAppender,
            String fileNamePattern) {
        val rollingPolicy = new FixedWindowRollingPolicy();

        rollingPolicy.setFileNamePattern(fileNamePattern);
        rollingPolicy.setMinIndex(1);
        rollingPolicy.setMaxIndex(MAX_LOG_FILE_COUNT);

        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.start();
        return rollingPolicy;
    }

    private TriggeringPolicy<ILoggingEvent> getTriggeringPolicy(LoggerContext loggerContext) {
        val triggeringPolicy = new SizeBasedTriggeringPolicy<ILoggingEvent>();

        triggeringPolicy.setMaxFileSize(MAX_LOG_SIZE);

        triggeringPolicy.setContext(loggerContext);
        triggeringPolicy.start();
        return triggeringPolicy;
    }
}
