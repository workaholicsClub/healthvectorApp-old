package ru.android.childdiary.presentation.main;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
