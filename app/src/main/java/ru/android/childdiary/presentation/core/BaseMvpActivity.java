package ru.android.childdiary.presentation.core;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.BuildConfig;
import ru.android.childdiary.R;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.utils.ui.ConfigUtils;
import ru.android.childdiary.utils.ui.ThemeUtils;

@SuppressLint("Registered")
public abstract class BaseMvpActivity<P extends BasePresenter> extends MvpAppCompatActivity implements BaseView {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @State
    protected Sex sex;

    protected void unsubscribeOnDestroy(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected abstract void injectActivity(ApplicationComponent applicationComponent);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ConfigUtils.setupOrientation(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        logger.debug("onCreate");
        setupDagger();
    }

    private void setupDagger() {
        ApplicationComponent component = ChildDiaryApplication.getApplicationComponent();
        injectActivity(component);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setupToolbar();
        }
        themeChanged();
    }


    @CallSuper
    protected void setupToolbar() {
        toolbar.setTitleTextAppearance(this, R.style.ToolbarTitleTextAppearance);
        toolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitleTextAppearance);
    }

    @CallSuper
    protected void themeChanged() {
    }

    private void setupToolbarColor() {
        logger.debug("theme changed");
        if (toolbar != null) {
            toolbar.setBackgroundColor(ThemeUtils.getColorPrimary(this, sex));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ThemeUtils.getColorPrimaryDark(this, sex));
        }
    }

    protected final void changeThemeIfNeeded(@Nullable Child child) {
        Sex sex = child == null ? null : child.getSex();
        changeThemeIfNeeded(sex);
    }

    protected final void changeThemeIfNeeded(@Nullable Sex sex) {
        if (this.sex != sex) {
            this.sex = sex;
            setupToolbarColor();
            themeChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logger.debug("onActivityResult");
    }

    @Override
    protected void onStart() {
        super.onStart();
        logger.debug("onStart");
        setupToolbarColor();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logger.debug("onSaveInstanceState");
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        logger.debug("onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.debug("onDestroy");
        compositeDisposable.dispose();
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        logger.error("unexpected error", e);
        if (BuildConfig.DEBUG) {
            new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(sex))
                    .setMessage(e.toString())
                    .setPositiveButton(R.string.OK, null)
                    .show();
        }
    }

    protected void showToast(String text) {
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
