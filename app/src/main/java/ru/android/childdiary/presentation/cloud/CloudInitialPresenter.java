package ru.android.childdiary.presentation.cloud;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.cloud.core.CloudPresenter;

@InjectViewState
public class CloudInitialPresenter extends CloudPresenter<CloudInitialView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}