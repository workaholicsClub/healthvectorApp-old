package ru.android.childdiary.presentation.core;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import butterknife.ButterKnife;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.presentation.main.MainActivity;
import ru.android.childdiary.presentation.profile.edit.ProfileEditActivity;

@SuppressLint("Registered")
public abstract class BaseActivity<P extends BasePresenter> extends MvpAppCompatActivity implements BaseActivityView {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    protected abstract void injectActivity(ApplicationComponent applicationComponent);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.debug("onCreate");

        setupDagger();

        setupActionBar();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    private void setupDagger() {
        ApplicationComponent component = ChildDiaryApplication.getApplicationComponent();
        injectActivity(component);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            boolean hasParent = getParent() != null;
            actionBar.setDisplayHomeAsUpEnabled(hasParent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logger.debug("onPause");
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
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public final void navigateToMain(Child child, List<Child> childList) {
        Intent intent = MainActivity.getIntent(this, child, childList);
        startActivity(intent);
    }

    @Override
    public final void navigateToProfileEdit(@Nullable Child child) {
        Intent intent = ProfileEditActivity.getIntent(this, child);
        startActivity(intent);
    }
}
