package ru.android.childdiary.utils.log;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
class CrashReportSystemAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    @Setter
    private PatternLayoutEncoder encoder;

    @Override
    public void start() {
        if (encoder == null || encoder.getLayout() == null) {
            addError("No layout set for the appender " + toString());
            return;
        }

        super.start();
    }

    @Override
    public void append(ILoggingEvent event) {
        if (!started) {
            return;
        }

        String msg = encoder.getLayout().doLayout(event);
        CrashReportSystem.log(msg);
    }
}
