package ru.android.childdiary.data.cloud;

import android.content.Context;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.data.repositories.core.images.ImagesDataRepository;
import ru.android.childdiary.domain.interactors.core.images.ImagesRepository;
import ru.android.childdiary.utils.io.FileUtils;
import ru.android.childdiary.utils.io.ZipUtils;

public class BackupService extends CloudService {
    private static final String UPLOAD_FIELDS = "id";

    private final Drive drive;
    private final ImagesRepository imagesRepository;

    @Inject
    public BackupService(Context context, Drive drive,
                         ImagesDataRepository imagesRepository) {
        super(context);
        this.drive = drive;
        this.imagesRepository = imagesRepository;
    }

    public Single<java.io.File> prepare() {
        return Single.fromCallable(() -> {
            // get images directory
            java.io.File imagesDir = imagesRepository.getImagesDir();
            // create images directory if there's no images in it, to zip at least one item
            FileUtils.mkdirs(imagesDir);

            // get db file
            java.io.File db = getDbFile();

            // create backup
            java.io.File zipFile = getNewBackupFile();
            ZipUtils.zipDirectory(new java.io.File[]{imagesDir, db}, zipFile);

            logger.debug("created backup: " + zipFile);

            return zipFile;
        });
    }

    public Single<Boolean> upload(java.io.File zipFile) {
        return Single.fromCallable(() -> {
            try {
                File fileMetadata = new File();
                fileMetadata.setName(BACKUP_FILE_NAME);
                fileMetadata.setParents(Collections.singletonList(APP_DATA_FOLDER));
                FileContent mediaContent = new FileContent(CONTENT_TYPE, zipFile);
                fileMetadata = drive.files().create(fileMetadata, mediaContent)
                        .setFields(UPLOAD_FIELDS)
                        .execute();

                if (BuildConfig.COPY_BACKUP_TO_EXTERNAL_STORAGE) {
                    java.io.File externalStorage = context.getExternalFilesDir(null);
                    FileUtils.copyFile(zipFile, new java.io.File(externalStorage, zipFile.getName()));
                }

                logger.debug("uploaded successfully, file id: " + fileMetadata.getId());

                return true;
            } finally {
                FileUtils.delete(zipFile);
            }
        });
    }
}
