package ru.android.healthvector.presentation.development.partitions.achievements.edit;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;
import ru.android.healthvector.domain.development.achievement.requests.DeleteConcreteAchievementRequest;
import ru.android.healthvector.domain.development.achievement.requests.DeleteConcreteAchievementResponse;
import ru.android.healthvector.domain.development.achievement.requests.UpsertConcreteAchievementRequest;
import ru.android.healthvector.domain.development.achievement.requests.UpsertConcreteAchievementResponse;
import ru.android.healthvector.presentation.development.partitions.achievements.core.ConcreteAchievementPresenter;

@InjectViewState
public class EditConcreteAchievementPresenter extends ConcreteAchievementPresenter<EditConcreteAchievementView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void update(@NonNull ConcreteAchievement concreteAchievement) {
        unsubscribeOnDestroy(
                concreteAchievementInteractor.update(UpsertConcreteAchievementRequest.builder()
                        .concreteAchievement(concreteAchievement)
                        .build())
                        .map(UpsertConcreteAchievementResponse::getConcreteAchievement)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::updated, this::onUnexpectedError));
    }

    public void delete(@NonNull ConcreteAchievement concreteAchievement) {
        getViewState().confirmDelete(concreteAchievement);
    }

    public void forceDelete(@NonNull ConcreteAchievement concreteAchievement) {
        unsubscribeOnDestroy(
                concreteAchievementInteractor.delete(DeleteConcreteAchievementRequest.builder()
                        .concreteAchievement(concreteAchievement)
                        .build())
                        .map(DeleteConcreteAchievementResponse::getRequest)
                        .map(DeleteConcreteAchievementRequest::getConcreteAchievement)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::deleted, this::onUnexpectedError));
    }
}
