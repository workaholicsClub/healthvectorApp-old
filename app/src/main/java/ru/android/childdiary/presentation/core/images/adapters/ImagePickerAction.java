package ru.android.childdiary.presentation.core.images.adapters;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ImagePickerAction {
    @NonNull
    ImagePickerActionType type;
    @StringRes
    int titleResourceId;
    @DrawableRes
    int iconResourceId;
}
