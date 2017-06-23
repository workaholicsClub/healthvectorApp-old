package ru.android.childdiary.presentation.cloud;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.cloud.core.CloudPresenter;

@InjectViewState
public class CloudOperationPresenter extends CloudPresenter<CloudOperationView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void moveNext() {
        getViewState().navigateToMain();
    }
}
