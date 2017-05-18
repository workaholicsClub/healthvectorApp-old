package ru.android.childdiary.data.repositories.core.images;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import ru.android.childdiary.domain.interactors.core.images.ImageType;
import ru.android.childdiary.domain.interactors.core.images.ImagesRepository;

@Singleton
public class ImagesDataRepository implements ImagesRepository {
    private static final String PARENT_DIR_NAME_IMAGES = "images";
    private static final String PARENT_DIR_NAME_PROFILE = PARENT_DIR_NAME_IMAGES + File.separator + "profile";
    private static final String PARENT_DIR_NAME_DOCTOR_VISIT = PARENT_DIR_NAME_IMAGES + File.separator + "doctor_visit";
    private static final String PARENT_DIR_NAME_DOCTOR_VISIT_EVENT = PARENT_DIR_NAME_IMAGES + File.separator + "doctor_visit_event";
    private static final String PARENT_DIR_NAME_MEDICINE_TAKING = PARENT_DIR_NAME_IMAGES + File.separator + "medicine_taking";
    private static final String PARENT_DIR_NAME_MEDICINE_TAKING_EVENT = PARENT_DIR_NAME_IMAGES + File.separator + "medicine_taking_event";
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
    public boolean isTemporaryImageFile(@Nullable String imageFileName) {
        try {
            return !TextUtils.isEmpty(imageFileName) && new File(context.getFilesDir(), imageFileName)
                    .getName()
                    .contains(CROPPED_IMAGE_FILE_NAME);
        } catch (Exception e) {
            throw new ImagesException("failed to check file is temporary", e);
        }
    }

    @Override
    public String createUniqueImageFileRelativePath(@NonNull ImageType imageType, @NonNull String relativePath) {
        try {
            // создаем целевой файл
            File parentDir = new File(context.getFilesDir(), getParentDirName(imageType));
            boolean result = parentDir.mkdirs();
            logger.debug(parentDir.getAbsolutePath() + " was" + (result ? "" : "n't") + " created");
            File resultFile = new File(parentDir, getPrefix() + IMAGE_FILE_SUFFIX);

            // переименовываем
            File fromFile = new File(context.getFilesDir(), relativePath);
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
    public Single<String> createTemporaryImageFileRelativePath(@NonNull String fromPath) {
        return Single.fromCallable(() -> {
            try {
                int len = context.getFilesDir().getAbsolutePath().length();
                return fromPath.substring(len);
            } catch (Exception e) {
                throw new ImagesException("failed to create temporary image file: "
                        + context.getFilesDir() + File.separator
                        + CROPPED_IMAGE_FILE_NAME, e);
            }
        });
    }

    @Override
    public Single<File> createCroppedImageFile() {
        return Single.fromCallable(() -> {
            try {
                File file = new File(context.getFilesDir(), CROPPED_IMAGE_FILE_NAME);
                return file;
            } catch (Exception e) {
                throw new ImagesException("failed to create cropped image file: "
                        + context.getFilesDir() + File.separator
                        + CROPPED_IMAGE_FILE_NAME, e);
            }
        });
    }

    @Override
    public Single<File> createCapturedImageFile() {
        return Single.fromCallable(() -> {
            try {
                File parentDir = new File(context.getCacheDir(), CACHE_ROOT);
                boolean result = parentDir.mkdirs();
                logger.debug(parentDir.getAbsolutePath() + " was" + (result ? "" : "n't") + " created");
                File file = new File(parentDir, CAPTURED_IMAGE_FILE_NAME);
                result = file.createNewFile();
                logger.debug(file.getAbsolutePath() + " was" + (result ? "" : "n't") + " created");
                return file;
            } catch (Exception e) {
                throw new ImagesException("failed to create captured image file: "
                        + context.getCacheDir() + File.separator
                        + CACHE_ROOT + File.separator
                        + CAPTURED_IMAGE_FILE_NAME, e);
            }
        });
    }

    @Override
    public void deleteImageFile(@Nullable String relativePath) {
        if (relativePath == null) {
            return;
        }
        File file = new File(context.getFilesDir(), relativePath);
        boolean result = file.delete();
        if (result) {
            logger.debug(file.getAbsolutePath() + " was deleted");
        } else {
            logger.error(file.getAbsolutePath() + " wasn't deleted");
        }
    }

    @Override
    public void deleteImageFiles(@NonNull List<String> relativePaths) {
        for (String relativePath : relativePaths) {
            deleteImageFile(relativePath);
        }
    }
}
