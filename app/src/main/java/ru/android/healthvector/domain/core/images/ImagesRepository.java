package ru.android.healthvector.domain.core.images;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.List;

import io.reactivex.Single;

public interface ImagesRepository {
    File getImagesDir();

    File getImagesParentDir();

    boolean isTemporaryImageFile(@Nullable String imageFileName);

    String createUniqueImageFileRelativePath(@NonNull ImageType imageType, @NonNull String fromPath);

    Single<String> createTemporaryImageFileRelativePath(@NonNull String fromPath);

    Single<File> createCroppedImageFile();

    Single<File> createCapturedImageFile();

    void deleteImageFile(@Nullable String relativePath);

    void deleteImageFiles(@NonNull List<String> relativePaths);
}
