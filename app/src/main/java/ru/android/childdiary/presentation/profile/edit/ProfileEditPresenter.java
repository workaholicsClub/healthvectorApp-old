package ru.android.childdiary.presentation.profile.edit;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class ProfileEditPresenter extends BasePresenter<ProfileEditView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
