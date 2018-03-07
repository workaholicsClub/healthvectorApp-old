package ru.android.healthvector.domain.core.images;

import android.support.annotation.NonNull;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.android.healthvector.data.repositories.core.images.ImagesDataRepository;

public class ImagesInteractor {
    private final ImagesRepository imagesRepository;

    @Inject
    public ImagesInteractor(ImagesDataRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
    }

    public Single<String> createTemporaryImageFile(@NonNull String fromPath) {
        return imagesRepository.createTemporaryImageFileRelativePath(fromPath);
    }

    public Single<File> createCroppedImageFile() {
        return imagesRepository.createCroppedImageFile();
    }

    public Single<File> createCapturedImageFile() {
        return imagesRepository.createCapturedImageFile();
    }
}
