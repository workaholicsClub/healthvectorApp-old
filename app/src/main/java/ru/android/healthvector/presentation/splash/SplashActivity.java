package ru.android.healthvector.presentation.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.presentation.cloud.CloudInitialActivity;
import ru.android.healthvector.presentation.core.BaseMvpActivity;
import ru.android.healthvector.presentation.main.AppPartition;
import ru.android.healthvector.presentation.main.MainActivity;
import ru.android.healthvector.presentation.onboarding.AppIntroActivity;

public class SplashActivity extends BaseMvpActivity implements SplashView {
    @InjectPresenter
    SplashPresenter presenter;

    public static Intent getIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

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
    public void navigateToMain(@Nullable Sex sex) {
        Intent intent = MainActivity.getIntent(this, AppPartition.CALENDAR, sex);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToAppIntro(@Nullable Sex sex) {
        Intent intent = AppIntroActivity.getIntent(this, true);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToCloud(@Nullable Sex sex) {
        Intent intent = CloudInitialActivity.getIntent(this, sex);
        startActivity(intent);
        finish();
    }
}
