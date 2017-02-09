package ru.android.childdiary.presentation.core;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;

import butterknife.ButterKnife;
import ru.android.childdiary.BuildConfig;

@SuppressLint("Registered")
public abstract class BaseActivity<P extends BasePresenter> extends MvpAppCompatActivity implements BaseActivityView {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            boolean hasParent = getParent() != null;
            actionBar.setDisplayHomeAsUpEnabled(hasParent);
        }
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
