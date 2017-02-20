package ru.android.childdiary.presentation.core;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.ButterKnife;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.di.ApplicationComponent;

@SuppressLint("Registered")
public abstract class BaseMvpActivity<P extends BasePresenter> extends MvpAppCompatActivity implements BaseActivityView {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    protected abstract void injectActivity(ApplicationComponent applicationComponent);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.debug("onCreate");

        setupDagger();
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
}
