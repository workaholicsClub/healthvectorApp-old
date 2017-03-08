package ru.android.childdiary.presentation.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.main.MainActivity;

public class SplashActivity extends BaseMvpActivity<SplashPresenter> implements SplashView {
    @InjectPresenter
    SplashPresenter presenter;

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void startApp() {
        finish();
        navigateToMain();
    }

    private void navigateToMain() {
        Intent intent = MainActivity.getIntent(this);
        startActivity(intent);
    }
}
