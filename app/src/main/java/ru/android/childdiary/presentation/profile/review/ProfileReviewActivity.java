package ru.android.childdiary.presentation.profile.review;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseActivity;

public class ProfileReviewActivity extends BaseActivity<ProfileReviewPresenter> implements ProfileReviewView {
    @InjectPresenter
    ProfileReviewPresenter presenter;

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
