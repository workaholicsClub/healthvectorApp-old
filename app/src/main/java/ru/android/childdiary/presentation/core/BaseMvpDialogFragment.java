package ru.android.childdiary.presentation.core;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.R;
import ru.android.childdiary.utils.KeyboardUtils;
import ru.android.childdiary.utils.log.LogSystem;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class BaseMvpDialogFragment<T extends BaseDialogArguments> extends MvpAppCompatDialogFragment implements BaseView {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    private final Map<Integer, RequestPermissionInfo> permissionInfoMap = new HashMap<>();

    protected T dialogArguments;

    @Nullable
    @BindView(R.id.dummy)
    View dummy;

    public void showAllowingStateLoss(FragmentManager manager, String tag, T dialogArguments) {
        Bundle data = new Bundle();
        data.putSerializable(ExtraConstants.EXTRA_DIALOG_ARGUMENTS, dialogArguments);
        setArguments(data);
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //noinspection unchecked
        dialogArguments = (T) getArguments().getSerializable(ExtraConstants.EXTRA_DIALOG_ARGUMENTS);
        Icepick.restoreInstanceState(this, savedInstanceState);

        View view = null;
        if (getLayoutResourceId() != 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(getLayoutResourceId(), null);

            ButterKnife.bind(this, view);

            setupUi();
        }

        return createDialog(view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        LogSystem.report(logger, "unexpected error", e);
        if (BuildConfig.DEBUG) {
            new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                    .setMessage(e.toString())
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    }

    public final void hideKeyboardAndClearFocus(@Nullable View view) {
        KeyboardUtils.hideKeyboard(getContext(), view);
        if (view != null) {
            view.clearFocus();
        }
        if (dummy != null) {
            dummy.requestFocus();
        }
    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    protected abstract void setupUi();

    @NonNull
    protected abstract Dialog createDialog(View view);

    protected final void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected final void requestPermission(RequestPermissionInfo permissionInfo) {
        String permission = permissionInfo.getPermission();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(getContext(), permission)
                != PackageManager.PERMISSION_GRANTED) {
            permissionInfoMap.put(permissionInfo.getRequestCode(), permissionInfo);
            if (shouldShowRequestPermissionRationale(permission)) {
                new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                        .setTitle(permissionInfo.getTitleResourceId())
                        .setMessage(permissionInfo.getTextResourceId())
                        .setPositiveButton(R.string.ok,
                                (DialogInterface dialog, int which) -> requestPermissions(new String[]{permission}, permissionInfo.getRequestCode()))
                        .setNegativeButton(R.string.cancel, null)
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

    protected void grantPermissionToApps(Intent intent, Uri uri) {
        Context context = getContext();
        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info : activities) {
            String packageName = info.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    protected void revokePermissions(Uri uri) {
        Context context = getContext();
        context.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }
}
