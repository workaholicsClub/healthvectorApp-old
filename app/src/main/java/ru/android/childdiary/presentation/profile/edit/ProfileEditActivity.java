package ru.android.childdiary.presentation.profile.edit;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BaseActivity;

public class ProfileEditActivity extends BaseActivity<ProfileEditPresenter> implements ProfileEditView {
    @InjectPresenter
    ProfileEditPresenter presenter;

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
