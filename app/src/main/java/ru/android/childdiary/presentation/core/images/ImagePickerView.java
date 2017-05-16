package ru.android.childdiary.presentation.core.images;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.io.File;

import ru.android.childdiary.presentation.core.BaseView;

public interface ImagePickerView extends BaseView {
    @StateStrategyType(OneExecutionStateStrategy.class)
    void setResultImageFile(@NonNull String relativePath);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void failedToCreateResultImageFile();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void startCrop(@NonNull Uri sourceUri, @NonNull File destinationFile);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void failedToCreateCropImageFile();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void startCamera(@NonNull File file);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void failedToCreateCameraImageFile();
}
