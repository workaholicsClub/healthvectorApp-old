package ru.android.healthvector.presentation.development.partitions.achievements.add;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.data.types.AchievementType;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.development.achievement.data.ConcreteAchievement;
import ru.android.healthvector.domain.development.achievement.requests.UpsertConcreteAchievementRequest;
import ru.android.healthvector.domain.development.achievement.requests.UpsertConcreteAchievementResponse;
import ru.android.healthvector.presentation.core.bindings.FieldValueChangeEventsObservable;
import ru.android.healthvector.presentation.development.partitions.achievements.core.ConcreteAchievementPresenter;

@InjectViewState
public class AddConcreteAchievementPresenter extends ConcreteAchievementPresenter<AddConcreteAchievementView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void add(@NonNull ConcreteAchievement concreteAchievement) {
        unsubscribeOnDestroy(
                concreteAchievementInteractor.add(UpsertConcreteAchievementRequest.builder()
                        .concreteAchievement(concreteAchievement)
                        .build())
                        .map(UpsertConcreteAchievementResponse::getConcreteAchievement)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::added, this::onUnexpectedError));
    }

    public Disposable listenForDoneButtonUpdate(
            @NonNull Observable<TextViewAfterTextChangeEvent> nameObservable,
            @NonNull FieldValueChangeEventsObservable<AchievementType> achievementTypeObservable) {
        return concreteAchievementInteractor.controlDoneButton(
                nameObservable, achievementTypeObservable)
                .subscribe(getViewState()::setButtonDoneEnabled, this::onUnexpectedError);
    }
}
