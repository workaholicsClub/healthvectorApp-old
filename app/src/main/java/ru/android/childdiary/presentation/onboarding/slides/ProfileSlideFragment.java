package ru.android.childdiary.presentation.onboarding.slides;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.onboarding.slides.core.SlideFragment;

public class ProfileSlideFragment extends SlideFragment {
    @Override
    protected int getTextResourceId() {
        return R.string.intention_add_child_profile;
    }

    @Override
    protected int getImageResourceId() {
        return R.drawable.onboarding_profile;
    }
}
