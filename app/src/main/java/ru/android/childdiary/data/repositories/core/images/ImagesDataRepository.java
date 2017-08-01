package ru.android.childdiary.data.repositories.core.images;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
import ru.android.childdiary.utils.io.FileUtils;

@Singleton
public class ImagesDataRepository implements ImagesRepository {
    private static final String PARENT_DIR_NAME_IMAGES = "images";
    private static final String PARENT_DIR_NAME_PROFILE = PARENT_DIR_NAME_IMAGES + File.separator + "profile";
    private static final String PARENT_DIR_NAME_DOCTOR_VISIT = PARENT_DIR_NAME_IMAGES + File.separator + "doctor_visit";
    private static final String PARENT_DIR_NAME_DOCTOR_VISIT_EVENT = PARENT_DIR_NAME_IMAGES + File.separator + "doctor_visit_event";
    private static final String PARENT_DIR_NAME_MEDICINE_TAKING = PARENT_DIR_NAME_IMAGES + File.separator + "medicine_taking";
    private static final String PARENT_DIR_NAME_MEDICINE_TAKING_EVENT = PARENT_DIR_NAME_IMAGES + File.separator + "medicine_taking_event";
    private static final String PARENT_DIR_NAME_EXERCISE_EVENT = PARENT_DIR_NAME_IMAGES + File.separator + "exercise_event";
    private static final String PARENT_DIR_NAME_ACHIEVEMENT = PARENT_DIR_NAME_IMAGES + File.separator + "achievement";

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

    /**
     * Don't change it. Specified in file_path.xml.
     *
     * @return images directory, i.e. /data/data/package/files/images/
     */
    @Override
    public File getImagesDir() {
        return new File(getImagesParentDir(), PARENT_DIR_NAME_IMAGES);
    }

    /**
     * Don't change it. Specified in file_path.xml.
     *
     * @return images directory, i.e. /data/data/package/files/
     */
    @Override
    public File getImagesParentDir() {
        return context.getFilesDir();
    }

    @Override
    public boolean isTemporaryImageFile(@Nullable String imageFileName) {
        return imageFileName != null && imageFileName.contains(CROPPED_IMAGE_FILE_NAME);
    }

    @Override
    public String createUniqueImageFileRelativePath(@NonNull ImageType imageType, @NonNull String relativePath) {
        try {
            // создаем целевой файл
            File parentDir = new File(getImagesParentDir(), getParentDirName(imageType));
            FileUtils.mkdirs(parentDir);
            File resultFile = new File(parentDir, getPrefix() + IMAGE_FILE_SUFFIX);

            // переименовываем
            File fromFile = new File(getImagesParentDir(), relativePath);
            FileUtils.move(fromFile, resultFile);

            // возвращаем относительный путь (относительно files directory приложения)
            // т.к. фотографии будут копироваться в облако, откуда могут быть восстановлены на другом устройстве
            // в общем случае путь до files directory может отличаться на разных устройствах
            int len = getImagesParentDir().getAbsolutePath().length();
            return resultFile.getAbsolutePath().substring(len);
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
            case EXERCISE_EVENT:
                return PARENT_DIR_NAME_EXERCISE_EVENT;
            case ACHIEVEMENT:
                return PARENT_DIR_NAME_ACHIEVEMENT;
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
                int len = getImagesParentDir().getAbsolutePath().length();
                return fromPath.substring(len);
            } catch (Exception e) {
                throw new ImagesException("failed to create temporary image file: "
                        + getImagesParentDir().getAbsolutePath() + File.separator
                        + CROPPED_IMAGE_FILE_NAME, e);
            }
        });
    }

    @Override
    public Single<File> createCroppedImageFile() {
        return Single.fromCallable(() -> {
            try {
                File file = new File(getImagesParentDir(), CROPPED_IMAGE_FILE_NAME);
                return file;
            } catch (Exception e) {
                throw new ImagesException("failed to create cropped image file: "
                        + getImagesParentDir().getAbsolutePath() + File.separator
                        + CROPPED_IMAGE_FILE_NAME, e);
            }
        });
    }

    @Override
    public Single<File> createCapturedImageFile() {
        return Single.fromCallable(() -> {
            try {
                File parentDir = new File(context.getCacheDir(), CACHE_ROOT);
                FileUtils.mkdirs(parentDir);
                File file = new File(parentDir, CAPTURED_IMAGE_FILE_NAME);
                FileUtils.createNewFile(file);
                return file;
            } catch (Exception e) {
                throw new ImagesException("failed to create captured image file: "
                        + context.getCacheDir().getAbsolutePath() + File.separator
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
        File file = new File(getImagesParentDir(), relativePath);
        FileUtils.delete(file);
    }

    @Override
    public void deleteImageFiles(@NonNull List<String> relativePaths) {
        //noinspection Convert2streamapi
        for (String relativePath : relativePaths) {
            deleteImageFile(relativePath);
        }
    }
}
