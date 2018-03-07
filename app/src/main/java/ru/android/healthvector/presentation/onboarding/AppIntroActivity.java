package ru.android.healthvector.presentation.onboarding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.presentation.cloud.CloudInitialActivity;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.main.AppPartition;
import ru.android.healthvector.presentation.main.MainActivity;
import ru.android.healthvector.presentation.onboarding.core.BaseAppIntroActivity;
import ru.android.healthvector.presentation.onboarding.slides.AchievementsSlideFragment;
import ru.android.healthvector.presentation.onboarding.slides.CalendarSlideFragment;
import ru.android.healthvector.presentation.onboarding.slides.ChartsSlideFragment;
import ru.android.healthvector.presentation.onboarding.slides.DoctorVisitsSlideFragment;
import ru.android.healthvector.presentation.onboarding.slides.ProfileSlideFragment;

public class AppIntroActivity extends BaseAppIntroActivity implements AppIntroView {
    @Getter
    private final List<Fragment> slides = Arrays.asList(
            new ProfileSlideFragment(),
            new CalendarSlideFragment(),
            new AchievementsSlideFragment(),
            new DoctorVisitsSlideFragment(),
            new ChartsSlideFragment()
    );

    @InjectPresenter
    AppIntroPresenter presenter;

    private boolean init;

    public static Intent getIntent(Context context, boolean init) {
        return new Intent(context, AppIntroActivity.class)
                .putExtra(ExtraConstants.EXTRA_INIT, init);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        init = getIntent().getBooleanExtra(ExtraConstants.EXTRA_INIT, false);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSkipPressed() {
        if (init) {
            presenter.skipPressed();
        } else {
            finish();
        }
    }

    @Override
    protected void onDonePressed() {
        if (init) {
            presenter.donePressed();
        } else {
            finish();
        }
    }

    @Override
    protected int getSlidesCount() {
        return slides.size();
    }

    @Override
    public void navigateToMain(@Nullable Sex sex) {
        Intent intent = MainActivity.getIntent(this, AppPartition.CALENDAR, sex);
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
