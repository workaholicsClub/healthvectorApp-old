package ru.android.childdiary.presentation.onboarding.slides;

import ru.android.childdiary.R;
import ru.android.childdiary.presentation.onboarding.slides.core.SlideFragment;

public class DoctorVisitsSlideFragment extends SlideFragment {
    @Override
    protected int getTextResourceId() {
        return R.string.app_intro_doctor_visits;
    }

    @Override
    protected int getImageResourceId() {
        return R.drawable.onboarding_doctor_visits;
    }
}
