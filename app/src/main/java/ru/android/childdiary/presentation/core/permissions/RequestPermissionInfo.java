package ru.android.childdiary.presentation.core.permissions;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class RequestPermissionInfo {
    @NonNull
    String permission;
    @NonNull
    String title;
    @NonNull
    String text;
    int requestCode;
}
