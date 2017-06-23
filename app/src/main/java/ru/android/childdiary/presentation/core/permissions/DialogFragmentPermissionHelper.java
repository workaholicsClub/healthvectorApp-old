package ru.android.childdiary.presentation.core.permissions;

import android.support.annotation.NonNull;

import ru.android.childdiary.presentation.core.BaseMvpDialogFragment;

public class DialogFragmentPermissionHelper extends PermissionHelper {
    private final BaseMvpDialogFragment dialogFragment;

    public DialogFragmentPermissionHelper(BaseMvpDialogFragment dialogFragment) {
        super(dialogFragment.getContext(), dialogFragment);
        this.dialogFragment = dialogFragment;
    }

    @Override
    protected boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return dialogFragment.shouldShowRequestPermissionRationale(permission);
    }

    @Override
    protected void requestPermissions(@NonNull String[] permissions, int requestCode) {
        dialogFragment.requestPermissions(permissions, requestCode);
    }
}
