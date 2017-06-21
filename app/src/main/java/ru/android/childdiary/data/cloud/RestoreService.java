package ru.android.childdiary.data.cloud;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import javax.inject.Inject;

import io.reactivex.Single;

public class RestoreService {
    private final Drive drive;

    @Inject
    public RestoreService(Drive drive) {
        this.drive = drive;
    }

    public Single<Boolean> checkIsBackupAvailable() {
        return Single.fromCallable(() -> {
            FileList files = drive.files().list()
                    .setSpaces("appDataFolder")
                    .setFields("nextPageToken, files(id, name)")
                    .setPageSize(10)
                    .execute();
            for (File file : files.getFiles()) {
                System.out.printf("Found file: %s (%s)\n",
                        file.getName(), file.getId());
            }
            return true;
        });
    }

    public void download() {

    }

    public void restore() {

    }
}
