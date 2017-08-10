package ru.android.childdiary.presentation.onboarding.slides;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.onboarding.slides.core.SlideFragment;

public class ChartsSlideFragment extends SlideFragment {
    @Override
    protected int getTextResourceId() {
        return R.string.app_intro_charts;
    }

    @Override
    protected int getImageResourceId() {
        return R.drawable.onboarding_charts;
    }
}
