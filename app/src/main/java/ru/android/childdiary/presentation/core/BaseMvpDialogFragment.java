package ru.android.childdiary.presentation.core;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.R;
import ru.android.childdiary.presentation.core.permissions.DialogFragmentPermissionHelper;
import ru.android.childdiary.presentation.core.permissions.PermissionHelper;
import ru.android.childdiary.presentation.core.permissions.RequestPermissionInfo;
import ru.android.childdiary.utils.KeyboardUtils;
import ru.android.childdiary.utils.log.LogSystem;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class BaseMvpDialogFragment<T extends BaseDialogArguments> extends MvpAppCompatDialogFragment
        implements BaseView, PermissionHelper.PermissionListener {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    protected PermissionHelper permissionHelper;

    protected T dialogArguments;

    @Nullable
    @BindView(R.id.rootView)
    View rootView;

    @Nullable
    @BindView(R.id.dummy)
    View dummy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionHelper = new DialogFragmentPermissionHelper(this);
    }

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

            setupUi(savedInstanceState);
        }

        return createDialog(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logger.debug("onSaveInstanceState");
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        LogSystem.report(logger, "unexpected error", e);
        if (BuildConfig.SHOW_ERROR_DIALOGS) {
            new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(dialogArguments.getSex()))
                    .setMessage(e.toString())
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    }

    public final void hideKeyboardAndClearFocus() {
        if (rootView == null) {
            logger.error("specify root view");
            return;
        }
        hideKeyboardAndClearFocus(rootView.findFocus());
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

    protected abstract void setupUi(@Nullable Bundle savedInstanceState);

    @NonNull
    protected abstract Dialog createDialog(@Nullable View view, @Nullable Bundle savedInstanceState);

    protected final void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void permissionGranted(RequestPermissionInfo permissionInfo) {
    }

    @Override
    public void permissionDenied(RequestPermissionInfo permissionInfo) {
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(0, 0);
    }
}
