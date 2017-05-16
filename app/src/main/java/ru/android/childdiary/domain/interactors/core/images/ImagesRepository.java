package ru.android.childdiary.domain.interactors.core.images;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;

import io.reactivex.Single;

public interface ImagesRepository {
    Single<String> createUniqueImageFile(@NonNull ImageType imageType, @NonNull Uri fromFileUri);

    Single<File> createCroppedImageFile();

    Single<File> createCapturedImageFile();
}
