package ru.android.childdiary.data.cloud;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import lombok.Cleanup;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.data.repositories.core.images.ImagesDataRepository;
import ru.android.childdiary.domain.interactors.core.images.ImagesRepository;
import ru.android.childdiary.utils.io.FileUtils;
import ru.android.childdiary.utils.io.ZipUtils;

public class RestoreService extends CloudService {
    private static final String SEARCH_QUERY = "name='" + BACKUP_FILE_NAME + "'";
    private static final String SEARCH_FIELDS = "files(id, name)";

    private final Drive drive;
    private final ImagesRepository imagesRepository;

    @Inject
    public RestoreService(Context context, Drive drive,
                          ImagesDataRepository imagesRepository) {
        super(context);
        this.drive = drive;
        this.imagesRepository = imagesRepository;
    }

    @Nullable
    private String getBackupFileId() throws IOException {
        FileList response = drive.files().list()
                .setSpaces(APP_DATA_FOLDER)
                .setQ(SEARCH_QUERY)
                .setFields(SEARCH_FIELDS)
                .execute();
        List<File> files = response == null ? null : response.getFiles();
        return files == null || files.isEmpty() ? null : files.get(0).getId();
    }

    public Single<Boolean> checkIsBackupAvailable() {
        return Single.fromCallable(() -> {
            String backupFileId = getBackupFileId();
            return !TextUtils.isEmpty(backupFileId);
        });
    }

    public Single<java.io.File> download() {
        return Single.fromCallable(() -> {
            String backupFileId = getBackupFileId();
            if (TextUtils.isEmpty(backupFileId)) {
                throw new BackupUnavailableException("No backup file found");
            }

            java.io.File file = getNewBackupFile();
            @Cleanup FileOutputStream outputStream = new FileOutputStream(file);
            drive.files()
                    .get(backupFileId)
                    .executeMediaAndDownloadTo(outputStream);

            logger.debug("downloaded successfully, file: " + file.getAbsolutePath());

            return file;
        });
    }

    public Single<Boolean> restore(java.io.File zipFile) {
        return Single.fromCallable(() -> {
            try {
                // delete old images directory
                java.io.File imagesDir = imagesRepository.getImagesDir();
                FileUtils.delete(imagesDir);

                // extract images and database
                java.io.File imagesParentDir = imagesRepository.getImagesParentDir();
                ZipUtils.extract(zipFile, imagesParentDir);
                java.io.File newDb = new java.io.File(imagesParentDir, BuildConfig.DB_NAME);

                // replace database
                java.io.File db = getDbFile();
                FileUtils.move(newDb, db);

                logger.debug("extracted from backup: " + zipFile.getAbsolutePath());

                return true;
            } finally {
                FileUtils.delete(zipFile);
            }
        });
    }
}
