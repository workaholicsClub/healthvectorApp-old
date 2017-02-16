package ru.android.childdiary.presentation.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.compat.BuildConfig;
import android.view.View;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<P extends BasePresenter> extends MvpAppCompatFragment implements BaseFragmentView {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    private Unbinder unbinder;

    public BaseFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        logger.error("unexpected error", e);
        if (BuildConfig.DEBUG) {
            showToast(e.toString());
        }
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
