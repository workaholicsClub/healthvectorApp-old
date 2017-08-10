package ru.android.childdiary.presentation.onboarding.slides;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.onboarding.slides.core.SlideFragment;

public class CalendarSlideFragment extends SlideFragment {
    @Override
    protected int getTextResourceId() {
        return R.string.app_intro_calendar;
    }

    @Override
    protected int getImageResourceId() {
        return R.drawable.onboarding_calendar;
    }
}
