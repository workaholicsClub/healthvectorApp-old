package ru.android.childdiary.presentation.profile.edit.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.UUID;

class ImagePickerHelper {
    private static final Logger logger = LoggerFactory.getLogger(ImagePickerHelper.class);
    private static final String IMAGE_FILE_SUFFIX = ".jpg";
    private static final String CROPPED_IMAGE_FILE_NAME = "cropped" + IMAGE_FILE_SUFFIX;
    private static final String CAPTURED_IMAGE_FILE_NAME = "captured" + IMAGE_FILE_SUFFIX;
    private static final String PARENT_DIR_NAME = "images";

    @Nullable
    public static File createUniqueImageFile(Context context, Uri resultUri) {
        try {
            String prefix = UUID.randomUUID().toString();
            File parentDir = new File(context.getFilesDir(), PARENT_DIR_NAME);
            parentDir.mkdirs();
            File resultFile = new File(parentDir, prefix + IMAGE_FILE_SUFFIX);
            File fromCropped = new File(resultUri.getPath());
            boolean result = fromCropped.renameTo(resultFile);
            if (result) {
                return resultFile;
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
    public static File getCroppedImageFile(Context context) {
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
    public static File createCapturedImageFile(Context context) {
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

    public static void grantPermissionToApps(Context context, Intent intent, Uri uri) {
        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info : activities) {
            String packageName = info.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    public static void revokePermissions(Context context, Uri uri) {
        context.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }
}
