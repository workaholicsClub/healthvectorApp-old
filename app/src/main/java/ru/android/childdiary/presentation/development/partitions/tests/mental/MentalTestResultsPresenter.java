package ru.android.childdiary.presentation.development.partitions.tests.mental;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryPresenter;

@InjectViewState
public class MentalTestResultsPresenter extends BaseDevelopmentDiaryPresenter<MentalTestResultsView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
