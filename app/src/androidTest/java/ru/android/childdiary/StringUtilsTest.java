package ru.android.childdiary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import ru.android.childdiary.utils.StringUtils;

public class StringUtilsTest {
    private final Logger logger = LoggerFactory.getLogger(toString());

    @Test
    public void printNotifyTime() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        for (int i = 0; i < 360; ++i) {
            logger.debug(String.format(Locale.US, "%5d: %s", i, StringUtils.notifyTime(appContext, i)));
        }
        for (int i = 24 * 60; i < 3 * 24 * 60; i += 23) {
            logger.debug(String.format(Locale.US, "%5d: %s", i, StringUtils.notifyTime(appContext, i)));
        }
    }
}
