package ru.android.healthvector.presentation.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lombok.AccessLevel;
import lombok.Getter;
import ru.android.healthvector.BuildConfig;
import ru.android.healthvector.R;
import ru.android.healthvector.app.ChildDiaryApplication;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.presentation.core.dialogs.ProgressDialogArguments;
import ru.android.healthvector.presentation.core.dialogs.ProgressDialogFragment;
import ru.android.healthvector.presentation.core.fields.widgets.FieldEditTextView;
import ru.android.healthvector.presentation.core.permissions.ActivityPermissionHelper;
import ru.android.healthvector.presentation.core.permissions.PermissionHelper;
import ru.android.healthvector.presentation.core.permissions.RequestPermissionInfo;
import ru.android.healthvector.utils.KeyboardUtils;
import ru.android.healthvector.utils.log.LogSystem;
import ru.android.healthvector.utils.ui.ThemeUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

@SuppressLint("Registered")
public abstract class BaseMvpActivity extends MvpAppCompatActivity
        implements BaseView, PermissionHelper.PermissionListener {
    protected final Logger logger = LoggerFactory.getLogger(toString());

    protected final PermissionHelper permissionHelper = new ActivityPermissionHelper(this);

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @State
    @Nullable
    @Getter(AccessLevel.PROTECTED)
    Sex sex;

    @Nullable
    @BindView(R.id.toolbar)
    @Getter(AccessLevel.PROTECTED)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.rootView)
    View rootView;

    @Nullable
    @BindView(R.id.dummy)
    View dummy;

    @BindDimen(R.dimen.base_margin)
    int margin;

    private ImageView toolbarLogo;

    private TextView toolbarTitle;

    protected Tracker analyticsTracker;

    protected void unsubscribeOnDestroy(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected abstract void injectActivity(ApplicationComponent applicationComponent);

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Icepick.restoreInstanceState(this, savedInstanceState);
        if (savedInstanceState == null) {
            sex = (Sex) getIntent().getSerializableExtra(ExtraConstants.EXTRA_SEX);
            if (sex == null) {
                Child child = (Child) getIntent().getSerializableExtra(ExtraConstants.EXTRA_CHILD);
                sex = child == null ? null : child.getSex();
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        logger.debug("onCreate");
        setupDagger();
        setupTracker();
    }

    protected void setupTracker() {
        ChildDiaryApplication application = (ChildDiaryApplication) getApplication();
        analyticsTracker = application.getDefaultTracker();
    }

    private void setupDagger() {
        ApplicationComponent component = ChildDiaryApplication.getApplicationComponent();
        injectActivity(component);
    }

    public void trackScreen(String screenName) {
        analyticsTracker.setScreenName(screenName);
        analyticsTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(layoutResId);
        setContentViewBeforeBind();
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setupToolbar(toolbar);
        }
        themeChanged();
    }

    protected void setContentViewBeforeBind() {
    }

    @CallSuper
    protected void setupToolbar(Toolbar toolbar) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbarLogo = ButterKnife.findById(toolbar, R.id.toolbarLogo);
        toolbarLogo.setVisibility(View.GONE);
        toolbarTitle = ButterKnife.findById(toolbar, R.id.toolbarTitle);
        toolbarTitle.setText(R.string.app_name);
    }

    public final void setupToolbarLogo(@Nullable Drawable drawable) {
        toolbarLogo.setVisibility(View.VISIBLE);
        toolbarLogo.setImageDrawable(drawable);
    }

    protected final void hideToolbarLogo() {
        toolbarLogo.setImageDrawable(null);
        toolbarLogo.setVisibility(View.GONE);
    }

    protected final void setupToolbarLogo(@DrawableRes int drawableRes) {
        toolbarLogo.setVisibility(View.VISIBLE);
        toolbarLogo.setImageDrawable(ContextCompat.getDrawable(this, drawableRes));
    }

    public final void setupToolbarTitle(String text) {
        toolbarTitle.setText(text);
    }

    public final void setupToolbarTitle(@StringRes int titleRes) {
        toolbarTitle.setText(titleRes);
    }

    private void addToolbarMargin() {
        Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) toolbarTitle.getLayoutParams();
        layoutParams.rightMargin = margin;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginEnd(margin);
        }
    }

    protected final void removeToolbarMargin() {
        Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) toolbarTitle.getLayoutParams();
        layoutParams.rightMargin = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginEnd(0);
        }
    }

    @CallSuper
    protected void themeChanged() {
        logger.debug("setup theme: " + sex);
    }

    private void setupToolbarColor() {
        logger.debug("setup toolbar color");
        if (toolbar != null) {
            toolbar.setBackgroundColor(ThemeUtils.getColorPrimary(this, sex));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ThemeUtils.getColorPrimaryDark(this, sex));
            }
        }
    }

    protected final void changeThemeIfNeeded(@Nullable Child child) {
        Sex sex = child == null ? null : child.getSex();
        changeThemeIfNeeded(sex);
    }

    protected final void changeThemeIfNeeded(@Nullable Sex sex) {
        if (this.sex != sex) {
            logger.debug("theme switched");
            this.sex = sex;
            setupToolbarColor();
            themeChanged();
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
        KeyboardUtils.hideKeyboard(this, view);
        if (view != null) {
            view.clearFocus();
        }
        if (dummy != null) {
            dummy.requestFocus();
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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        logger.debug("onRestoreInstanceState");
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
        LogSystem.report(logger, "unexpected error", e);
        if (BuildConfig.SHOW_ERROR_DIALOGS) {
            new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(sex))
                    .setMessage(e.toString())
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    }

    public final void showToast(String text) {
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

    public void showProgress(String tag, String title, String message) {
        ProgressDialogFragment dialogFragment = findProgressDialog(tag);
        if (dialogFragment == null) {
            dialogFragment = new ProgressDialogFragment();
            dialogFragment.showAllowingStateLoss(getSupportFragmentManager(),
                    tag,
                    ProgressDialogArguments.builder()
                            .sex(getSex())
                            .title(title)
                            .message(message)
                            .build());
        }
    }

    public void hideProgress(String tag) {
        ProgressDialogFragment dialogFragment = findProgressDialog(tag);
        if (dialogFragment != null) {
            dialogFragment.dismissAllowingStateLoss();
        }
    }

    private ProgressDialogFragment findProgressDialog(String tag) {
        return (ProgressDialogFragment) getSupportFragmentManager().findFragmentByTag(tag);
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
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        addToolbarMargin();
        return super.onCreateOptionsMenu(menu);
    }

    protected final void setupEditTextView(FieldEditTextView view) {
        List<Disposable> disposables = view.createSubscriptions(this::hideKeyboardAndClearFocus);
        //noinspection Convert2streamapi
        for (Disposable disposable : disposables) {
            unsubscribeOnDestroy(disposable);
        }
    }
}
