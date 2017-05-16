package ru.android.childdiary.data.repositories.core.images;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.android.childdiary.domain.interactors.core.images.ImageType;
import ru.android.childdiary.domain.interactors.core.images.ImagesRepository;

public class ImagesDataRepository implements ImagesRepository {
    private static final String PARENT_DIR_NAME_PROFILE = "profile";
    private static final String PARENT_DIR_NAME_DOCTOR_VISIT = "doctor_visit";
    private static final String PARENT_DIR_NAME_DOCTOR_VISIT_EVENT = "doctor_visit_event";
    private static final String PARENT_DIR_NAME_MEDICINE_TAKING = "medicine_taking";
    private static final String PARENT_DIR_NAME_MEDICINE_TAKING_EVENT = "medicine_taking_event";
    // TODO EXERCISE

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy_MM_dd'T'HH_mm_ss_SSS");

    private static final String IMAGE_FILE_SUFFIX = ".jpg";

    /**
     * This subdirectory name is specified in file_paths.xml for FileProvider.
     */
    private static final String CACHE_ROOT = "images";
    private static final String CROPPED_IMAGE_FILE_NAME = "cropped" + IMAGE_FILE_SUFFIX;
    private static final String CAPTURED_IMAGE_FILE_NAME = "captured" + IMAGE_FILE_SUFFIX;

    private final Logger logger = LoggerFactory.getLogger(toString());
    private final Context context;

    @Inject
    public ImagesDataRepository(Context context) {
        this.context = context;
    }

    @Override
    public Single<String> createUniqueImageFile(@NonNull ImageType imageType, @NonNull Uri fromFileUri) {
        return Single.fromCallable(() -> {
            try {
                // создаем целевой файл
                File parentDir = new File(getRootDir(), getParentDirName(imageType));
                boolean result = parentDir.mkdirs();
                logger.debug(parentDir.getAbsolutePath() + "was" + (result ? "" : "n't") + " created");
                File resultFile = new File(parentDir, getPrefix() + IMAGE_FILE_SUFFIX);

                // переименовываем
                File fromFile = new File(fromFileUri.getPath());
                result = fromFile.renameTo(resultFile);
                if (result) {
                    // возвращаем относительный путь (относительно files directory приложения)
                    // т.к. фотографии будут копироваться в облако, откуда могут быть восстановлены на другом устройстве
                    // в общем случае путь до files directory может отличаться на разных устройствах
                    int len = context.getFilesDir().getAbsolutePath().length();
                    return resultFile.getAbsolutePath().substring(len);
                } else {
                    throw new ImagesException("failed to rename from " + fromFile + " to " + resultFile);
                }
            } catch (Exception e) {
                throw new ImagesException("failed to create unique file", e);
            }
        });
    }

    private File getRootDir() {
        return context.getFilesDir();
    }

    private String getParentDirName(@NonNull ImageType imageType) {
        switch (imageType) {
            case PROFILE:
                return PARENT_DIR_NAME_PROFILE;
            case DOCTOR_VISIT:
                return PARENT_DIR_NAME_DOCTOR_VISIT;
            case DOCTOR_VISIT_EVENT:
                return PARENT_DIR_NAME_DOCTOR_VISIT_EVENT;
            case MEDICINE_TAKING:
                return PARENT_DIR_NAME_MEDICINE_TAKING;
            case MEDICINE_TAKING_EVENT:
                return PARENT_DIR_NAME_MEDICINE_TAKING_EVENT;
            // TODO EXERCISE
        }
        throw new IllegalArgumentException("Unsupported image type");
    }

    private String getPrefix() {
        return DATE_TIME_FORMATTER.print(DateTime.now());
    }

    @Override
    public Single<File> createCroppedImageFile() {
        return createTempImageFile(CROPPED_IMAGE_FILE_NAME);
    }

    @Override
    public Single<File> createCapturedImageFile() {
        return createTempImageFile(CAPTURED_IMAGE_FILE_NAME);
    }

    private Single<File> createTempImageFile(@NonNull String fileName) {
        return Single.fromCallable(() -> {
            try {
                File parentDir = new File(context.getCacheDir(), CACHE_ROOT);
                boolean result = parentDir.mkdirs();
                logger.debug(parentDir.getAbsolutePath() + "was" + (result ? "" : "n't") + " created");
                File file = new File(parentDir, fileName);
                result = file.createNewFile();
                logger.debug(file.getAbsolutePath() + "was" + (result ? "" : "n't") + " created");
                return file;
            } catch (Exception e) {
                throw new ImagesException("failed to create temp image file: "
                        + CACHE_ROOT + File.separator + fileName, e);
            }
        });
    }
}
