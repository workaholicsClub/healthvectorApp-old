package ru.android.childdiary.presentation.development.partitions.tests.physical;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryPresenter;

@InjectViewState
public class PhysicalTestResultsPresenter extends BaseDevelopmentDiaryPresenter<PhysicalTestResultsView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
