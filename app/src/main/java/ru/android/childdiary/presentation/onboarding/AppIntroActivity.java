package ru.android.childdiary.presentation.onboarding;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.Arrays;
import java.util.List;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.onboarding.core.BaseAppIntroActivity;
import ru.android.childdiary.presentation.onboarding.slides.AchievementsSlideFragment;
import ru.android.childdiary.presentation.onboarding.slides.CalendarSlideFragment;
import ru.android.childdiary.presentation.onboarding.slides.ChartsSlideFragment;
import ru.android.childdiary.presentation.onboarding.slides.DoctorVisitsSlideFragment;
import ru.android.childdiary.presentation.onboarding.slides.ProfileSlideFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AppIntroActivity extends BaseAppIntroActivity {
    public static Intent getIntent(Context context) {
        return new Intent(context, AppIntroActivity.class);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
    protected List<Fragment> getSlides() {
        return Arrays.asList(
                new ProfileSlideFragment(),
                new CalendarSlideFragment(),
                new AchievementsSlideFragment(),
                new DoctorVisitsSlideFragment(),
                new ChartsSlideFragment()
        );
    }
}
