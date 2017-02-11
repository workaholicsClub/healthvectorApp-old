package ru.android.childdiary.utils.log;

import android.util.Log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;

@NoArgsConstructor
class CrashReportSystemAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    @Getter
    @Setter
    private PatternLayoutEncoder encoder;

    @Getter
    @Setter
    private PatternLayoutEncoder tagEncoder;

    @Override
    public void start() {
        if (encoder == null || encoder.getLayout() == null) {
            addError("No layout set for the appender " + toString());
            return;
        }

        if (tagEncoder != null) {
            val layout = tagEncoder.getLayout();

            if (layout == null) {
                addError("No tag layout set for the appender " + toString());
                return;
            }

            if (layout instanceof PatternLayout) {
                String pattern = tagEncoder.getPattern();
                if (!pattern.contains("%nopex")) {
                    tagEncoder.stop();
                    tagEncoder.setPattern(pattern + "%nopex");
                    tagEncoder.start();
                }

                val tagLayout = (PatternLayout) layout;
                tagLayout.setPostCompileProcessor(null);
            }
        }

        super.start();
    }

    @Override
    public void append(ILoggingEvent event) {
        if (!started) {
            return;
        }

        String tag = getTag(event);
        String msg = encoder.getLayout().doLayout(event);

        switch (event.getLevel().levelInt) {
            case Level.ALL_INT:
            case Level.TRACE_INT:
                CrashReportSystem.log(Log.VERBOSE, tag, msg);
                break;
            case Level.DEBUG_INT:
                CrashReportSystem.log(Log.DEBUG, tag, msg);
                break;
            case Level.INFO_INT:
                CrashReportSystem.log(Log.INFO, tag, msg);
                break;
            case Level.WARN_INT:
                CrashReportSystem.log(Log.WARN, tag, msg);
                break;
            case Level.ERROR_INT:
                CrashReportSystem.log(Log.ERROR, tag, msg);
                break;
        }
    }

    private String getTag(ILoggingEvent event) {
        return tagEncoder == null ? event.getLoggerName() : tagEncoder.getLayout().doLayout(event);
    }
}
