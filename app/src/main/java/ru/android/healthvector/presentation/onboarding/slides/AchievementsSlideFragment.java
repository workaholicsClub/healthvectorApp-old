package ru.android.healthvector.presentation.onboarding.slides;

import ru.android.healthvector.R;
import ru.android.healthvector.presentation.onboarding.slides.core.SlideFragment;

public class AchievementsSlideFragment extends SlideFragment {
    @Override
    protected int getTextResourceId() {
        return R.string.app_intro_achievements;
    }

    @Override
    protected int getImageResourceId() {
        return R.drawable.onboarding_achievements;
    }
}
