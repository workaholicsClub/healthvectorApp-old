package ru.android.childdiary.presentation.core;

import android.support.annotation.NonNull;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RequestPermissionInfo {
    @NonNull
    String permission;
    int titleResourceId;
    int textResourceId;
    int requestCode;
}
