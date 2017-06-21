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

public class RestoreService {
    private final Drive drive;
    private final Context context;
    private final ImagesRepository imagesRepository;

    @Inject
    public RestoreService(Drive drive,
                          Context context,
                          ImagesDataRepository imagesRepository) {
        this.drive = drive;
        this.context = context;
        this.imagesRepository = imagesRepository;
    }

    public Single<Boolean> checkIsBackupAvailable() {
        return Single.fromCallable(() -> {
            java.io.File imagesParentDir = imagesRepository.getImagesParentDir();
            java.io.File imagesDir = imagesRepository.getImagesDir();
            // create images directory if there's no images in it, to zip at least one item
            FileUtils.mkdirs(imagesDir);
            java.io.File db = context.getDatabasePath(BuildConfig.DB_NAME);
            // create images directory backup
            java.io.File zipFile = java.io.File.createTempFile("backup", ".zip", context.getCacheDir());
            ZipUtils.zipDirectory(new java.io.File[]{imagesDir, db}, zipFile);

            // delete old
            FileUtils.delete(imagesDir);
            // extract images
            ZipUtils.extract(zipFile, imagesParentDir);

            FileUtils.delete(zipFile);

            String backupFileId = getBackupFileId();
            return !TextUtils.isEmpty(backupFileId);
        });
    }

    @Nullable
    private String getBackupFileId() throws IOException {
        FileList response = drive.files().list()
                .setSpaces("appDataFolder")
                .setQ("name='backup.zip'")
                .setFields("files(id, name)")
                .execute();
        List<File> files = response == null ? null : response.getFiles();
        return files == null || files.isEmpty() ? null : files.get(0).getId();
    }

    public Single<java.io.File> download() {
        return Single.fromCallable(() -> {
            String backupFileId = getBackupFileId();
            if (TextUtils.isEmpty(backupFileId)) {
                throw new EmptyResponseException("No backup file found");
            }

            java.io.File file = java.io.File.createTempFile("backup", ".zip", context.getCacheDir());
            @Cleanup FileOutputStream outputStream = new FileOutputStream(file);
            drive.files()
                    .get(backupFileId)
                    .executeMediaAndDownloadTo(outputStream);
            return file;
        });
    }

    public Single<Boolean> restore(java.io.File file) {
        return Single.fromCallable(() -> true);
    }
}
