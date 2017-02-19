package ru.android.childdiary.presentation.profile.review;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseMvpActivity;

public class ProfileReviewActivity extends BaseMvpActivity<ProfileReviewPresenter> implements ProfileReviewView {
    @InjectPresenter
    ProfileReviewPresenter presenter;

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
