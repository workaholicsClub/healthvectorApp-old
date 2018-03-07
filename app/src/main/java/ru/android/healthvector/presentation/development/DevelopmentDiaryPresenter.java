package ru.android.healthvector.presentation.development;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.development.achievement.ConcreteAchievementInteractor;
import ru.android.healthvector.domain.development.antropometry.AntropometryInteractor;
import ru.android.healthvector.presentation.core.AppPartitionPresenter;

@InjectViewState
public class DevelopmentDiaryPresenter extends AppPartitionPresenter<DevelopmentDiaryView> {
    @Inject
    AntropometryInteractor antropometryInteractor;

    @Inject
    ConcreteAchievementInteractor concreteAchievementInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
