package ru.android.childdiary.presentation.core;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.compat.BuildConfig;
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
import ru.android.childdiary.R;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.utils.ui.ThemeUtils;

public abstract class BaseMvpFragment extends MvpAppCompatFragment implements BaseView {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.debug("onCreate");
        ApplicationComponent component = ChildDiaryApplication.getApplicationComponent();
        injectFragment(component);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logger.debug("onDestroyView");
        unbinder.unbind();
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        logger.error("unexpected error", e);
        if (BuildConfig.DEBUG) {
            new AlertDialog.Builder(getContext(), ThemeUtils.getThemeDialogRes(getSex()))
                    .setMessage(e.toString())
                    .setPositiveButton(R.string.OK, null)
                    .show();
        }
    }

    protected void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    protected abstract Sex getSex();

    protected void injectFragment(ApplicationComponent component) {
    }
}
