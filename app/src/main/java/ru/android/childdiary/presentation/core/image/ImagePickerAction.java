package ru.android.childdiary.presentation.core.image;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class ImagePickerAction {
    @NonNull
    ImagePickerActionType type;
    @StringRes
    int titleResourceId;
    @DrawableRes
    int iconResourceId;
}
