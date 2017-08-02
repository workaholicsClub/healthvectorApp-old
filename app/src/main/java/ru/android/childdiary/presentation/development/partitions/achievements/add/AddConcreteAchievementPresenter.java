package ru.android.childdiary.presentation.development.partitions.achievements.add;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.development.achievement.ConcreteAchievement;
import ru.android.childdiary.domain.interactors.development.achievement.requests.UpsertConcreteAchievementRequest;
import ru.android.childdiary.domain.interactors.development.achievement.requests.UpsertConcreteAchievementResponse;
import ru.android.childdiary.presentation.development.partitions.achievements.core.ConcreteAchievementPresenter;

@InjectViewState
public class AddConcreteAchievementPresenter extends ConcreteAchievementPresenter<AddConcreteAchievementView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void add(@NonNull ConcreteAchievement concreteAchievement) {
        unsubscribeOnDestroy(
                achievementInteractor.add(UpsertConcreteAchievementRequest.builder()
                        .concreteAchievement(concreteAchievement)
                        .build())
                        .map(UpsertConcreteAchievementResponse::getConcreteAchievement)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::added, this::onUnexpectedError));
    }

    public Disposable listenForDoneButtonUpdate(
            @NonNull Observable<TextViewAfterTextChangeEvent> nameObservable) {
        return achievementInteractor.controlDoneButtonConcreteAchievement(
                nameObservable)
                .subscribe(getViewState()::setButtonDoneEnabled, this::onUnexpectedError);
    }
}
