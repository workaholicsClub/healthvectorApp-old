package ru.android.childdiary.presentation.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
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
    public void startApp(@Nullable Sex sex) {
        finish();
        navigateToMain(sex);
    }

    private void navigateToMain(@Nullable Sex sex) {
        Intent intent = MainActivity.getIntent(this, sex);
        startActivity(intent);
    }
}
