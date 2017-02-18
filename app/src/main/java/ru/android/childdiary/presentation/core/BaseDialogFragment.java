package ru.android.childdiary.presentation.core;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
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

import ru.android.childdiary.R;
import ru.android.childdiary.utils.RequestPermissionInfo;

public abstract class BaseDialogFragment extends DialogFragment {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    private final Map<Integer, RequestPermissionInfo> permissionInfoMap = new HashMap<>();

    public BaseDialogFragment() {
    }

    public final void showAllowingStateLoss(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
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
                new AlertDialog.Builder(getActivity())
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
