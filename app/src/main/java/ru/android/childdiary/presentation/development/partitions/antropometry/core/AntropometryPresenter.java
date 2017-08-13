package ru.android.childdiary.presentation.development.partitions.antropometry.core;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.development.antropometry.AntropometryInteractor;
import ru.android.childdiary.domain.development.antropometry.validation.AntropometryValidationException;
import ru.android.childdiary.domain.development.antropometry.validation.AntropometryValidationResult;
import ru.android.childdiary.presentation.core.BasePresenter;
import ru.android.childdiary.presentation.core.bindings.FieldValueChangeEventsObservable;

public abstract class AntropometryPresenter<V extends AntropometryView> extends BasePresenter<V> {
    @Inject
    protected AntropometryInteractor antropometryInteractor;

    protected Child child;

    public void init(@NonNull Child child) {
        this.child = child;
    }

    public Disposable listenForFieldsUpdate(
            @NonNull FieldValueChangeEventsObservable<Double> heightObservable,
            @NonNull FieldValueChangeEventsObservable<Double> weightObservable) {
        return antropometryInteractor.controlFields(
                heightObservable,
                weightObservable)
                .subscribe(this::handleValidationResult, this::onUnexpectedError);
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        if (e instanceof AntropometryValidationException) {
            List<AntropometryValidationResult> results = ((AntropometryValidationException) e).getValidationResults();
            if (results.isEmpty()) {
                logger.error("validation results empty");
                return;
            }

            getViewState().validationFailed();
            String msg = Observable.fromIterable(results)
                    .filter(AntropometryValidationResult::notValid)
                    .map(AntropometryValidationResult::toString)
                    .blockingFirst();
            getViewState().showValidationErrorMessage(msg);
            handleValidationResult(results);
        } else {
            super.onUnexpectedError(e);
        }
    }

    public void handleValidationResult(List<AntropometryValidationResult> results) {
        boolean weightValid = true, heightValid = true;
        for (AntropometryValidationResult result : results) {
            boolean valid = result.isValid();
            if (result.getFieldType() == null) {
                continue;
            }
            switch (result.getFieldType()) {
                case HEIGHT_WEIGHT:
                    weightValid = weightValid && valid;
                    heightValid = heightValid && valid;
                    break;
                case HEIGHT:
                    heightValid = heightValid && valid;
                    break;
                case WEIGHT:
                    weightValid = weightValid && valid;
                    break;
            }
        }
        getViewState().heightValidated(heightValid);
        getViewState().weightValidated(weightValid);
    }
}
