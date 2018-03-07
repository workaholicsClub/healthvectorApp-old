package ru.android.healthvector.presentation.dictionaries.core;

import android.support.annotation.NonNull;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.android.healthvector.domain.dictionaries.core.DictionaryInteractor;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryFieldType;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryValidationException;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryValidationResult;
import ru.android.healthvector.presentation.core.BasePresenter;

public abstract class BaseAddPresenter<T, V extends BaseAddView<T>> extends BasePresenter<V> {
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        requestList();
    }

    private void requestList() {
        unsubscribeOnDestroy(getInteractor().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showList, this::onUnexpectedError));
    }

    public final void add(@NonNull T item) {
        unsubscribeOnDestroy(getInteractor().add(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::itemAdded, this::onUnexpectedError));
    }

    protected abstract DictionaryInteractor<T> getInteractor();

    protected abstract DictionaryFieldType getNameFieldType();

    public final Disposable listenForDoneButtonUpdate(@NonNull Observable<TextViewAfterTextChangeEvent> nameObservable) {
        return getInteractor().controlDoneButton(nameObservable)
                .subscribe(getViewState()::setButtonDoneEnabled, this::onUnexpectedError);
    }

    public final Disposable listenForFieldsUpdate(@NonNull Observable<TextViewAfterTextChangeEvent> nameObservable) {
        return getInteractor().controlFields(nameObservable)
                .subscribe(this::handleValidationResult, this::onUnexpectedError);
    }

    @Override
    public void onUnexpectedError(Throwable e) {
        if (e instanceof DictionaryValidationException) {
            List<DictionaryValidationResult> results = ((DictionaryValidationException) e).getValidationResults();
            if (results.isEmpty()) {
                logger.error("validation results empty");
                return;
            }

            getViewState().validationFailed();
            String msg = Observable.fromIterable(results)
                    .filter(DictionaryValidationResult::notValid)
                    .map(DictionaryValidationResult::toString)
                    .blockingFirst();
            getViewState().showValidationErrorMessage(msg);
            handleValidationResult(results);
        } else {
            super.onUnexpectedError(e);
        }
    }

    protected void handleValidationResult(List<DictionaryValidationResult> results) {
        for (DictionaryValidationResult result : results) {
            boolean valid = result.isValid();
            if (result.getFieldType() == null) {
                continue;
            }
            if (result.getFieldType() == getNameFieldType()) {
                getViewState().nameValidated(valid);
            }
        }
    }
}
