package ru.android.childdiary.presentation.core;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.R;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.utils.ui.ThemeUtils;

@SuppressLint("Registered")
public abstract class BaseMvpActivity<P extends BasePresenter> extends MvpAppCompatActivity implements BaseActivityView {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    protected Sex sex;

    protected void setSex(@Nullable Child child) {
        Sex sex = child == null ? null : child.getSex();
        setSex(sex);
    }

    protected void setSex(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            themeChanged();
        }
    }

    @CallSuper
    protected void themeChanged() {
        if (toolbar != null) {
            toolbar.setBackgroundColor(ThemeUtils.getColorPrimary(this, sex));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ThemeUtils.getColorPrimaryDark(this, sex));
        }
    }

    protected abstract void injectActivity(ApplicationComponent applicationComponent);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (sex == null) {
            sex = (Sex) getIntent().getSerializableExtra(ExtraConstants.EXTRA_SEX);
            if (sex == null) {
                Child child = getIntent().getParcelableExtra(ExtraConstants.EXTRA_CHILD);
                sex = child == null ? null : child.getSex();
            }
        }

        setTheme(ThemeUtils.getTheme(sex));
        super.onCreate(savedInstanceState);
        logger.debug("onCreate");

        Icepick.restoreInstanceState(this, savedInstanceState);

        setupDagger();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setupToolbar();
    }

    private void setupDagger() {
        ApplicationComponent component = ChildDiaryApplication.getApplicationComponent();
        injectActivity(component);
    }

    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setupToolbar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            setupActionBar(actionBar);
        }
    }

    protected void setupToolbar(Toolbar toolbar) {
    }

    protected void setupActionBar(ActionBar actionBar) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
