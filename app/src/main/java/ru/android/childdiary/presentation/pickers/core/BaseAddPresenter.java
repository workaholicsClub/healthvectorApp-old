package ru.android.childdiary.presentation.pickers.core;

import android.support.annotation.NonNull;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.domain.core.validation.EventValidationException;
import ru.android.childdiary.domain.core.validation.EventValidationResult;
import ru.android.childdiary.domain.interactors.medical.MedicalDictionaryInteractor;
import ru.android.childdiary.presentation.core.BasePresenter;

public abstract class BaseAddPresenter<T, V extends BaseAddView<T>> extends BasePresenter<V> {
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        requestList();
    }

    private void requestList() {
        unsubscribeOnDestroy(getAllItemsLoader()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showList, this::onUnexpectedError));
    }

    public abstract void add(@NonNull T item);

    protected abstract Observable<List<T>> getAllItemsLoader();

    protected abstract MedicalDictionaryInteractor<T> getInteractor();

    public Disposable listenForDoneButtonUpdate(@NonNull Observable<TextViewAfterTextChangeEvent> nameObservable) {
        return getInteractor().controlDoneButton(nameObservable)
                .subscribe(getViewState()::setButtonDoneEnabled, this::onUnexpectedError);
    }

    public Disposable listenForFieldsUpdate(@NonNull Observable<TextViewAfterTextChangeEvent> nameObservable) {
        return getInteractor().controlFields(nameObservable)
                .subscribe(this::handleValidationResult, this::onUnexpectedError);
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        if (e instanceof EventValidationException) {
            List<EventValidationResult> results = ((EventValidationException) e).getValidationResults();
            if (results.isEmpty()) {
                logger.error("validation results empty");
                return;
            }

            getViewState().validationFailed();
            String msg = Observable.fromIterable(results)
                    .filter(EventValidationResult::notValid)
                    .map(EventValidationResult::toString)
                    .blockingFirst();
            getViewState().showValidationErrorMessage(msg);
            handleValidationResult(results);
        } else {
            super.onUnexpectedError(e);
        }
    }

    private void handleValidationResult(List<EventValidationResult> results) {
        for (EventValidationResult result : results) {
            boolean valid = result.isValid();
            if (result.getFieldType() == null) {
                continue;
            }
            switch (result.getFieldType()) {
                case DOCTOR_NAME:
                    getViewState().nameValidated(valid);
                    break;
                case MEDICINE_NAME:
                    getViewState().nameValidated(valid);
                    break;
            }
        }
    }
}
