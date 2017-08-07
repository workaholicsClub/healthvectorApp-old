package ru.android.childdiary.presentation.onboarding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.android.childdiary.presentation.onboarding.core.AppIntroBase;
import ru.android.childdiary.presentation.onboarding.slides.AchievementsSlideFragment;
import ru.android.childdiary.presentation.onboarding.slides.CalendarSlideFragment;
import ru.android.childdiary.presentation.onboarding.slides.ChartsSlideFragment;
import ru.android.childdiary.presentation.onboarding.slides.DoctorVisitsSlideFragment;
import ru.android.childdiary.presentation.onboarding.slides.ProfileSlideFragment;
import ru.android.childdiary.utils.ui.ConfigUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AppIntroActivity extends AppIntroBase {
    private final Logger logger = LoggerFactory.getLogger(toString());

    public static Intent getIntent(Context context) {
        return new Intent(context, AppIntroActivity.class);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        logger.debug("onCreate");
        ConfigUtils.setupOrientation(this);
        super.onCreate(savedInstanceState);

        addSlide(new ProfileSlideFragment());
        addSlide(new CalendarSlideFragment());
        addSlide(new AchievementsSlideFragment());
        addSlide(new DoctorVisitsSlideFragment());
        addSlide(new ChartsSlideFragment());
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        logger.debug("onSkipPressed: " + currentFragment);
        super.onSkipPressed(currentFragment);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        logger.debug("onDonePressed: " + currentFragment);
        super.onDonePressed(currentFragment);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        logger.debug("onSlideChanged: oldFragment = " + oldFragment + "; newFragment = " + newFragment);
        super.onSlideChanged(oldFragment, newFragment);
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
}
