package ru.android.childdiary.domain.interactors.core.images;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.List;

import io.reactivex.Single;

public interface ImagesRepository {
    Single<String> createUniqueImageFile(@NonNull ImageType imageType, @NonNull Uri fromFileUri);

    Single<File> createCroppedImageFile();

    Single<File> createCapturedImageFile();

    void deleteImageFile(@Nullable String relativePath);

    void deleteImageFiles(@NonNull List<String> relativePaths);
}
