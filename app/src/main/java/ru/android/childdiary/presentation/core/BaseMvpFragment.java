package ru.android.childdiary.presentation.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import icepick.Icepick;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.R;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.permissions.FragmentPermissionHelper;
import ru.android.childdiary.presentation.core.permissions.PermissionHelper;
import ru.android.childdiary.presentation.core.permissions.RequestPermissionInfo;
import ru.android.childdiary.utils.log.LogSystem;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class BaseMvpFragment extends MvpAppCompatFragment
        implements BaseView, PermissionHelper.PermissionListener {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    protected PermissionHelper permissionHelper;

    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        logger.debug("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.debug("onCreate");
        ApplicationComponent component = ChildDiaryApplication.getApplicationComponent();
        injectFragment(component);
        permissionHelper = new FragmentPermissionHelper(this);
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        logger.debug("onCreateView");
        init(savedInstanceState);
        return inflater.inflate(getLayoutResourceId(), container, false);
    }

    @CallSuper
    protected void init(@Nullable Bundle savedInstanceState) {
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logger.debug("onActivityCreated");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logger.debug("onActivityResult");
    }

    @Override
    public void onStart() {
        super.onStart();
        logger.debug("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        logger.debug("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        logger.debug("onPause");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logger.debug("onSaveInstanceState");
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        logger.debug("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logger.debug("onDestroyView");
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        logger.debug("onDetach");
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        LogSystem.report(logger, "unexpected error", e);
        if (BuildConfig.SHOW_ERROR_DIALOGS) {
            if (getContext() == null) {
                logger.error("context is null");
                return;
            }
            new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                    .setMessage(e.toString())
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    }

    protected final void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    protected abstract Sex getSex();

    protected void injectFragment(ApplicationComponent component) {
    }

    public void showProgress(String tag, String title, String message) {
        if (getActivity() == null) {
            logger.error("activity is null");
            return;
        }
        ((BaseMvpActivity) getActivity()).showProgress(tag, title, message);
    }

    public void hideProgress(String tag) {
        if (getActivity() == null) {
            logger.error("activity is null");
            return;
        }
        ((BaseMvpActivity) getActivity()).hideProgress(tag);
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
        if (getActivity() == null) {
            logger.error("activity is null");
            return;
        }
        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (getActivity() == null) {
            logger.error("activity is null");
            return;
        }
        getActivity().overridePendingTransition(0, 0);
    }
}
