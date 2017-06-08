package ru.android.childdiary.app;

import android.content.Context;
import android.os.Debug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import ru.android.childdiary.utils.DeviceUtils;

public class ChildDiaryUncaugthExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String DUMP_FILE_PREFIX = "cev_dump_";
    private static final String DUMP_FILE_SUFFIX = ".hprof";

    private final Logger logger = LoggerFactory.getLogger(toString());

    private final Context context;
    private final boolean dumpOutOfMemory;

    public ChildDiaryUncaugthExceptionHandler(Context context) {
        this(context, false);
    }

    public ChildDiaryUncaugthExceptionHandler(Context context, boolean dumpOutOfMemory) {
        this.context = context;
        this.dumpOutOfMemory = dumpOutOfMemory;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        logger.error("Unexpected error occurred in the thread " + thread, throwable);

        if (dumpOutOfMemory && isOutOfMemory(throwable)) {
            logger.debug("Try to make dump");
            dumpHprof();
        }

        System.exit(1);
    }

    private boolean isOutOfMemory(Throwable throwable) {
        // TODO: check exception stack trace contains substring "OutOfMemoryError"
        return throwable.getClass() == OutOfMemoryError.class
                || (throwable.getCause() != null && throwable.getCause().getClass() == OutOfMemoryError.class);
    }

    private void dumpHprof() {
        if (!DeviceUtils.isExternalStorageWritable()) {
            logger.debug("Dump failed: external storage is unavailable");
            return;
        }

        File file = new File(context.getExternalFilesDir(null),
                DUMP_FILE_PREFIX + System.currentTimeMillis() + DUMP_FILE_SUFFIX);
        String path = file.getAbsolutePath();
        logger.debug("Dumping to " + path);

        dumpHprof(path);
    }

    private void dumpHprof(String path) {
        try {
            Debug.dumpHprofData(path);
            logger.debug("Dump succeeded");
        } catch (Exception e) {
            logger.error("Dump failed", e);
        }
    }
}
