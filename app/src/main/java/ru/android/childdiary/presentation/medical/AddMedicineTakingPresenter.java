package ru.android.childdiary.presentation.medical;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class AddMedicineTakingPresenter extends BasePresenter<AddMedicineTakingView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
