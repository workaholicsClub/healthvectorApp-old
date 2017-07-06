package ru.android.childdiary.presentation.development;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;

@InjectViewState
public class DevelopmentDiaryPresenter extends AppPartitionPresenter<DevelopmentDiaryView> {
    // TODO inject interactors

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void addAchievement() {
        // TODO add achievement
    }

    public void openTestResult() {
        // TODO
    }

    public void openTest() {
        // TODO
    }

    public void addAntropometry() {
        // TODO add antropometry
    }
}
