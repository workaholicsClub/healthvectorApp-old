package ru.android.childdiary.presentation.core.images;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.core.images.ImagesInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class ImagePickerPresenter extends BasePresenter<ImagePickerView> {
    @Inject
    ImagesInteractor imagesInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void createTemporaryImageFile(@NonNull Uri fromFileUri) {
        unsubscribeOnDestroy(imagesInteractor.createTemporaryImageFile(fromFileUri.getPath())
                .doOnError(throwable -> logger.error("failed to create temporary image file", throwable))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::setResultImageFile,
                        throwable -> getViewState().failedToCreateResultImageFile()));
    }

    public void startCrop(@NonNull Uri sourceUri) {
        unsubscribeOnDestroy(imagesInteractor.createCroppedImageFile()
                .doOnError(throwable -> logger.error("failed to create cropped image file", throwable))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(destinationFile -> getViewState().startCrop(sourceUri, destinationFile),
                        throwable -> getViewState().failedToCreateCropImageFile()));
    }

    public void startCamera() {
        unsubscribeOnDestroy(imagesInteractor.createCapturedImageFile()
                .doOnError(throwable -> logger.error("failed to create captured image file", throwable))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::startCamera,
                        throwable -> getViewState().failedToCreateCameraImageFile()));
    }
}
