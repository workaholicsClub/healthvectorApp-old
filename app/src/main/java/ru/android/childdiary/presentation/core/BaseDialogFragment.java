package ru.android.childdiary.presentation.core;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import icepick.Icepick;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class BaseDialogFragment extends DialogFragment {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    private final Map<Integer, RequestPermissionInfo> permissionInfoMap = new HashMap<>();

    protected Child child;
    protected Sex sex;

    public void showAllowingStateLoss(FragmentManager manager, String tag, @Nullable Child child) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ExtraConstants.EXTRA_CHILD, child);
        setArguments(arguments);
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        child = getArguments().getParcelable(ExtraConstants.EXTRA_CHILD);
        sex = child == null ? null : child.getSex();
        Icepick.restoreInstanceState(this, savedInstanceState);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    protected void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    protected final void requestPermission(RequestPermissionInfo permissionInfo) {
        String permission = permissionInfo.getPermission();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(getActivity(), permission)
                != PackageManager.PERMISSION_GRANTED) {
            permissionInfoMap.put(permissionInfo.getRequestCode(), permissionInfo);
            if (shouldShowRequestPermissionRationale(permission)) {
                new AlertDialog.Builder(getActivity(), ThemeUtils.getThemeDialog(sex))
                        .setTitle(permissionInfo.getTitleResourceId())
                        .setMessage(permissionInfo.getTextResourceId())
                        .setPositiveButton(R.string.OK,
                                (DialogInterface dialog, int which) -> requestPermissions(new String[]{permission}, permissionInfo.getRequestCode()))
                        .setNegativeButton(R.string.Cancel, null)
                        .show();
            } else {
                requestPermissions(new String[]{permission}, permissionInfo.getRequestCode());
            }
        } else {
            permissionGranted(permissionInfo);
        }
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            RequestPermissionInfo permissionInfo = permissionInfoMap.get(requestCode);
            permissionGranted(permissionInfo);
        }
    }

    protected void permissionGranted(RequestPermissionInfo permissionInfo) {
    }
}
