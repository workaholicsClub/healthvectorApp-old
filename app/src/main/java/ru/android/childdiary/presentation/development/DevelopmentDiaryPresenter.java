package ru.android.childdiary.presentation.development;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.development.achievement.ConcreteAchievementInteractor;
import ru.android.childdiary.domain.development.antropometry.AntropometryInteractor;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;

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
