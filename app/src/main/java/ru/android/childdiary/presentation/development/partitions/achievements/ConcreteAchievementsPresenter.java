package ru.android.childdiary.presentation.development.partitions.achievements;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.data.repositories.core.exceptions.RestrictDeleteException;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.child.ChildInteractor;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.achievement.ConcreteAchievementInteractor;
import ru.android.childdiary.domain.development.achievement.data.ConcreteAchievement;
import ru.android.childdiary.domain.development.achievement.requests.DeleteConcreteAchievementRequest;
import ru.android.childdiary.domain.development.achievement.requests.DeleteConcreteAchievementResponse;
import ru.android.childdiary.presentation.development.partitions.core.BaseDevelopmentDiaryPresenter;

@InjectViewState
public class ConcreteAchievementsPresenter extends BaseDevelopmentDiaryPresenter<ConcreteAchievementsView> {
    @Inject
    ChildInteractor childInteractor;

    @Inject
    ConcreteAchievementInteractor concreteAchievementInteractor;

    private Disposable subscription;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        unsubscribeOnDestroy(
                childInteractor.getActiveChild()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::requestData, this::onUnexpectedError));
    }

    private void requestData(@NonNull Child child) {
        unsubscribe(subscription);
        subscription = unsubscribeOnDestroy(concreteAchievementInteractor.getConcreteAchievements(child)
                .map(concreteAchievements -> ConcreteAchievementsState.builder()
                        .child(child)
                        .concreteAchievements(concreteAchievements)
                        .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showConcreteAchievementsState, this::onUnexpectedError));
    }

    public void edit(@NonNull ConcreteAchievement concreteAchievement) {
        unsubscribeOnDestroy(
                childInteractor.getActiveChildOnce()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                child -> getViewState().navigateToConcreteAchievement(child, concreteAchievement),
                                this::onUnexpectedError));
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

    @Override
    public void onUnexpectedError(Throwable e) {
        if (e instanceof RestrictDeleteException) {
            getViewState().deletionRestrictedAchievement();
        } else {
            super.onUnexpectedError(e);
        }
    }
}
