package ru.android.childdiary.presentation.development.partitions.achievements.core;

import android.support.annotation.NonNull;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.development.achievement.AchievementInteractor;
import ru.android.childdiary.domain.interactors.dictionaries.GetAchievementsRequest;
import ru.android.childdiary.domain.interactors.development.achievement.validation.AchievementValidationException;
import ru.android.childdiary.domain.interactors.development.achievement.validation.AchievementValidationResult;
import ru.android.childdiary.presentation.core.BasePresenter;

public abstract class ConcreteAchievementPresenter<V extends ConcreteAchievementView> extends BasePresenter<V> {
    @Inject
    protected AchievementInteractor achievementInteractor;

    protected Child child;

    public void init(@NonNull Child child) {
        this.child = child;
        unsubscribeOnDestroy(achievementInteractor.getAchievements(GetAchievementsRequest.builder()
                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showAchievements, this::onUnexpectedError));
    }

    public Disposable listenForFieldsUpdate(
            @NonNull Observable<TextViewAfterTextChangeEvent> nameObservable) {
        return achievementInteractor.controlFieldsConcreteAchievement(
                nameObservable)
                .subscribe(this::handleValidationResult, this::onUnexpectedError);
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        if (e instanceof AchievementValidationException) {
            List<AchievementValidationResult> results = ((AchievementValidationException) e).getValidationResults();
            if (results.isEmpty()) {
                logger.error("concrete achievement validation results empty");
                return;
            }

            getViewState().validationFailed();
            String msg = Observable.fromIterable(results)
                    .filter(AchievementValidationResult::notValid)
                    .map(AchievementValidationResult::toString)
                    .blockingFirst();
            getViewState().showValidationErrorMessage(msg);
            handleValidationResult(results);
        } else {
            super.onUnexpectedError(e);
        }
    }

    public void handleValidationResult(List<AchievementValidationResult> results) {
        for (AchievementValidationResult result : results) {
            boolean valid = result.isValid();
            if (result.getFieldType() == null) {
                continue;
            }
            switch (result.getFieldType()) {
                case CONCRETE_ACHIEVEMENT_NAME:
                    getViewState().achievementNameValidated(valid);
                    break;
            }
        }
    }
}
