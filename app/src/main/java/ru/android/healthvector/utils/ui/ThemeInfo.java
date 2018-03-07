package ru.android.healthvector.utils.ui;

import android.support.annotation.ColorRes;
import android.support.annotation.StyleRes;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class ThemeInfo {
    @StyleRes
    int themeDialogResourceId;
    @ColorRes
    int colorPrimary;
    @ColorRes
    int colorPrimaryDark;
    @ColorRes
    int colorAccent;
}
