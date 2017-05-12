package ru.android.childdiary.presentation.medical.core;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.medical.validation.MedicalValidationException;
import ru.android.childdiary.domain.interactors.medical.validation.MedicalValidationResult;

public abstract class BaseAddItemPresenter<V extends BaseAddItemView<T>, T extends Serializable> extends BaseItemPresenter<V, T> {
    public abstract void add(@NonNull T item);

    @Override
    public void onUnexpectedError(Throwable e) {
        if (e instanceof MedicalValidationException) {
            List<MedicalValidationResult> results = ((MedicalValidationException) e).getValidationResults();
            if (results.isEmpty()) {
                logger.error("medical validation results empty");
                return;
            }

            getViewState().validationFailed();
            String msg = Observable.fromIterable(results)
                    .filter(MedicalValidationResult::notValid)
                    .map(MedicalValidationResult::toString)
                    .blockingFirst();
            getViewState().showValidationErrorMessage(msg);
            handleValidationResult(results);
        } else {
            super.onUnexpectedError(e);
        }
    }

    public void handleValidationResult(List<MedicalValidationResult> results) {
    }
}
