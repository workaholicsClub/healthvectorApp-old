package ru.android.childdiary.presentation.development;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.core.requests.ChildResponse;
import ru.android.childdiary.domain.development.achievement.ConcreteAchievementInteractor;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.domain.development.antropometry.AntropometryInteractor;
import ru.android.childdiary.domain.development.antropometry.data.Antropometry;
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

    public void addConcreteAchievement() {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .flatMap(child -> concreteAchievementInteractor.getDefaultConcreteAchievement(child)
                                .map(item -> new ChildResponse<>(child, item)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getChild().getId() == null) {
                                        getViewState().noChildSpecified();
                                        return;
                                    }
                                    ConcreteAchievement concreteAchievement = response.getResponse();
                                    getViewState().navigateToConcreteAchievementAdd(concreteAchievement.getChild(), concreteAchievement);
                                },
                                this::onUnexpectedError));
    }

    public void addAntropometry() {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .flatMap(child -> antropometryInteractor.getDefaultAntropometry(child)
                                .map(item -> new ChildResponse<>(child, item)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response.getChild().getId() == null) {
                                        getViewState().noChildSpecified();
                                        return;
                                    }
                                    Antropometry antropometry = response.getResponse();
                                    getViewState().navigateToAntropometryAdd(antropometry.getChild(), antropometry);
                                },
                                this::onUnexpectedError));
    }
}
