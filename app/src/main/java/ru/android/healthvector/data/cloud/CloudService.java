package ru.android.healthvector.data.cloud;

import android.content.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import ru.android.healthvector.BuildConfig;
import ru.android.healthvector.utils.io.FileUtils;

abstract class CloudService {
    protected final static String BACKUP_PREFIX = "backup";
    protected final static String BACKUP_SUFFIX = ".zip";
    protected final static String BACKUP_FILE_NAME = BACKUP_PREFIX + BACKUP_SUFFIX;

    protected final static String APP_DATA_FOLDER = "appDataFolder";
    protected final static String CONTENT_TYPE = "application/zip";

    protected final Logger logger = LoggerFactory.getLogger(toString());
    protected final Context context;

    protected CloudService(Context context) {
        this.context = context;
    }

    protected void cleanBackups() {
        FileUtils.deleteFiles(getBackupParentDir(),
                (File dir, String name) -> name.startsWith(BACKUP_PREFIX) && name.endsWith(BACKUP_SUFFIX));
    }

    protected File getBackupParentDir() {
        return context.getCacheDir();
    }

    protected File getDbFile() {
        return context.getDatabasePath(BuildConfig.DB_NAME);
    }

    protected File getNewBackupFile() throws IOException {
        return File.createTempFile(BACKUP_PREFIX, BACKUP_SUFFIX, getBackupParentDir());
    }
}
