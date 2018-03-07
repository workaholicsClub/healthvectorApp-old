package ru.android.healthvector.presentation.cloud;

import com.arellomobile.mvp.InjectViewState;

import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.presentation.cloud.core.CloudPresenter;

@InjectViewState
public class CloudInitialPresenter extends CloudPresenter<CloudInitialView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
