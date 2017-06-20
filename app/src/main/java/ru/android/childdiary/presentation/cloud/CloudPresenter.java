package ru.android.childdiary.presentation.cloud;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class CloudPresenter extends BasePresenter<CloudView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
