package ru.android.childdiary.presentation.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;

import javax.inject.Inject;

import ru.android.childdiary.R;
import ru.android.childdiary.app.ChildDiaryApplication;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseActivity;
import ru.android.childdiary.presentation.core.navigation.NavigationController;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {
    @InjectPresenter
    SplashPresenter mPresenter;

    @Inject
    NavigationController mNavigationController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        ApplicationComponent component = ChildDiaryApplication.getApplicationComponent();
        component.inject(this);
    }

    @Override
    public void navigateToMain() {
        mNavigationController.navigateToMain(this);
    }
}
