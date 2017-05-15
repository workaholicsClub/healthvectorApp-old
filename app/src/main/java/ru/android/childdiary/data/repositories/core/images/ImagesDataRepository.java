package ru.android.childdiary.data.repositories.core.images;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;

import ru.android.childdiary.domain.interactors.core.images.ImagesRepository;

public class ImagesDataRepository implements ImagesRepository {
    private final Logger logger = LoggerFactory.getLogger(toString());

    private static final String IMAGE_FILE_SUFFIX = ".jpg";
    private static final String CROPPED_IMAGE_FILE_NAME = "cropped" + IMAGE_FILE_SUFFIX;
    private static final String CAPTURED_IMAGE_FILE_NAME = "captured" + IMAGE_FILE_SUFFIX;
    private static final String PARENT_DIR_NAME = "images";

    @Nullable
    @Override
    public String createUniqueImageFile(Context context, Uri resultUri) {
        try {
            String prefix = UUID.randomUUID().toString();
            File parentDir = new File(context.getFilesDir(), PARENT_DIR_NAME);
            parentDir.mkdirs();
            File resultFile = new File(parentDir, prefix + IMAGE_FILE_SUFFIX);
            File fromCropped = new File(resultUri.getPath());
            boolean result = fromCropped.renameTo(resultFile);
            if (result) {
                int len = context.getFilesDir().getAbsolutePath().length();
                return resultFile.getAbsolutePath().substring(len);
            } else {
                logger.error("failed to rename from " + fromCropped + " to " + resultFile);
                return null;
            }
        } catch (Exception e) {
            logger.error("failed to create unique file", e);
            return null;
        }
    }

    @Nullable
    @Override
    public File getCroppedImageFile(Context context) {
        try {
            File parentDir = context.getCacheDir();
            File f = new File(parentDir, CROPPED_IMAGE_FILE_NAME);
            return f;
        } catch (Exception e) {
            logger.error("failed to create file for crop", e);
            return null;
        }
    }

    @Nullable
    @Override
    public File createCapturedImageFile(Context context) {
        try {
            File parentDir = new File(context.getCacheDir(), PARENT_DIR_NAME);
            parentDir.mkdirs();
            File f = new File(parentDir, CAPTURED_IMAGE_FILE_NAME);
            f.createNewFile();
            return f;
        } catch (Exception e) {
            logger.error("failed to create file for camera", e);
            return null;
        }
    }
}
