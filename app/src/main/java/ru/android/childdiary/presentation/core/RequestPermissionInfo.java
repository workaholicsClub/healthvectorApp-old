package ru.android.childdiary.presentation.core;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RequestPermissionInfo {
    String permission;
    int titleResourceId;
    int textResourceId;
    int requestCode;
}
