package ru.android.childdiary.domain.interactors.core.images;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.android.childdiary.data.repositories.core.images.ImagesDataRepository;

public class ImagesInteractor {
    private final ImagesRepository imagesRepository;

    @Inject
    public ImagesInteractor(ImagesDataRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
    }

    public Single<String> createUniqueImageFile(@NonNull ImageType imageType, @NonNull Uri fromFileUri) {
        return imagesRepository.createUniqueImageFile(imageType, fromFileUri);
    }

    public Single<File> createCroppedImageFile() {
        return imagesRepository.createCroppedImageFile();
    }

    public Single<File> createCapturedImageFile() {
        return imagesRepository.createCapturedImageFile();
    }
}
