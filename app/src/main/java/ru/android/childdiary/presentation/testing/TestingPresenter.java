package ru.android.childdiary.presentation.testing;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.development.testing.TestingInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

@InjectViewState
public class TestingPresenter extends BasePresenter<TestingView> {
    @Inject
    TestingInteractor testingInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
