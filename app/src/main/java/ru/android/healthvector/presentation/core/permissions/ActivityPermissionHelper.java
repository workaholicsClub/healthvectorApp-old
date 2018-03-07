package ru.android.healthvector.presentation.core.permissions;

import android.os.Build;
import android.support.annotation.NonNull;

import ru.android.healthvector.presentation.core.BaseMvpActivity;

public class ActivityPermissionHelper extends PermissionHelper {
    private final BaseMvpActivity activity;

    public ActivityPermissionHelper(BaseMvpActivity activity) {
        super(activity, activity);
        this.activity = activity;
    }

    @Override
    protected boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.shouldShowRequestPermissionRationale(permission);
    }

    @Override
    protected void requestPermissions(@NonNull String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(permissions, requestCode);
        }
    }
}
