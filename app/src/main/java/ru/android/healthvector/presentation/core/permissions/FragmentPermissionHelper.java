package ru.android.healthvector.presentation.core.permissions;

import android.support.annotation.NonNull;

import ru.android.healthvector.presentation.core.BaseMvpFragment;

public class FragmentPermissionHelper extends PermissionHelper {
    private final BaseMvpFragment fragment;

    public FragmentPermissionHelper(BaseMvpFragment fragment) {
        super(fragment.getContext(), fragment);
        this.fragment = fragment;
    }

    @Override
    protected boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return fragment.shouldShowRequestPermissionRationale(permission);
    }

    @Override
    protected void requestPermissions(@NonNull String[] permissions, int requestCode) {
        fragment.requestPermissions(permissions, requestCode);
    }
}
