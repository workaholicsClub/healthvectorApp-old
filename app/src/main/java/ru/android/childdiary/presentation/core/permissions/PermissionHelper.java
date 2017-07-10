package ru.android.childdiary.presentation.core.permissions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class PermissionHelper {
    private final Context context;
    private final PermissionListener listener;
    @SuppressLint("UseSparseArrays")
    private final Map<Integer, RequestPermissionInfo> permissionInfoMap = new HashMap<>();

    protected PermissionHelper(Context context, @NonNull PermissionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public final void requestPermission(@NonNull RequestPermissionInfo permissionInfo,
                                        @Nullable Sex sex) {
        String permission = permissionInfo.getPermission();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionInfoMap.put(permissionInfo.getRequestCode(), permissionInfo);
            if (shouldShowRequestPermissionRationale(permission)) {
                showRequestPermissionDialog(permissionInfo, sex);
            } else {
                requestPermissions(new String[]{permission}, permissionInfo.getRequestCode());
            }
        } else {
            listener.permissionGranted(permissionInfo);
        }
    }

    private void showRequestPermissionDialog(RequestPermissionInfo permissionInfo, @Nullable Sex sex) {
        String permission = permissionInfo.getPermission();
        new AlertDialog.Builder(context, ThemeUtils.getThemeDialogRes(sex))
                .setTitle(permissionInfo.getTitle())
                .setMessage(permissionInfo.getText())
                .setPositiveButton(R.string.ok,
                        (dialog, which) -> requestPermissions(new String[]{permission}, permissionInfo.getRequestCode()))
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> listener.permissionDenied(permissionInfo))
                .setOnKeyListener(
                        (dialog, keyCode, event) -> {
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                listener.permissionDenied(permissionInfo);
                                dialog.dismiss();
                                return true;
                            }
                            return false;
                        })
                .setCancelable(false)
                .show();
    }

    protected abstract boolean shouldShowRequestPermissionRationale(@NonNull String permission);

    protected abstract void requestPermissions(@NonNull String[] permissions, int requestCode);

    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        RequestPermissionInfo permissionInfo = permissionInfoMap.get(requestCode);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            listener.permissionGranted(permissionInfo);
        } else {
            listener.permissionDenied(permissionInfo);
        }
    }

    public final void grantPermissionToApps(Intent intent, Uri uri) {
        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info : activities) {
            String packageName = info.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    public final void revokePermissions(Uri uri) {
        context.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    public interface PermissionListener {
        void permissionGranted(RequestPermissionInfo permissionInfo);

        void permissionDenied(RequestPermissionInfo permissionInfo);
    }
}
