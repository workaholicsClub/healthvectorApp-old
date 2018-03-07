package ru.android.healthvector.presentation.development.partitions.achievements.core;

import android.support.annotation.NonNull;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.data.types.AchievementType;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.development.achievement.ConcreteAchievementInteractor;
import ru.android.healthvector.domain.development.achievement.validation.AchievementValidationException;
import ru.android.healthvector.domain.development.achievement.validation.AchievementValidationResult;
import ru.android.healthvector.domain.dictionaries.achievements.AchievementInteractor;
import ru.android.healthvector.presentation.core.BasePresenter;
import ru.android.healthvector.presentation.core.bindings.FieldValueChangeEventsObservable;

public abstract class ConcreteAchievementPresenter<V extends ConcreteAchievementView> extends BasePresenter<V> {
    @Inject
    protected AchievementInteractor achievementInteractor;

    @Inject
    protected ConcreteAchievementInteractor concreteAchievementInteractor;

    protected Child child;

    public void init(@NonNull Child child) {
        this.child = child;
        unsubscribeOnDestroy(achievementInteractor.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showAchievements, this::onUnexpectedError));
    }

    public Disposable listenForFieldsUpdate(
            @NonNull Observable<TextViewAfterTextChangeEvent> nameObservable,
            @NonNull FieldValueChangeEventsObservable<AchievementType> achievementTypeObservable) {
        return concreteAchievementInteractor.controlFields(
                nameObservable, achievementTypeObservable)
                .subscribe(this::handleValidationResult, this::onUnexpectedError);
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        if (e instanceof AchievementValidationException) {
            List<AchievementValidationResult> results = ((AchievementValidationException) e).getValidationResults();
            if (results.isEmpty()) {
                logger.error("validation results empty");
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
