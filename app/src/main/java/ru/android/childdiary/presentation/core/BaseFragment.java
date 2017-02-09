package ru.android.childdiary.presentation.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.compat.BuildConfig;
import android.view.View;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<P extends BasePresenter> extends MvpAppCompatFragment implements BaseView {
    private Unbinder mUnbinder;

    public BaseFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
