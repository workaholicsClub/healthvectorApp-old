package ru.android.childdiary.presentation.core.fields.dialogs.add.core;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.dictionaries.core.validation.DictionaryValidationException;
import ru.android.childdiary.domain.dictionaries.core.validation.DictionaryValidationResult;
import ru.android.childdiary.presentation.core.BasePresenter;

public abstract class AddValuePresenter<T, V extends AddValueView<T>> extends BasePresenter<V> {
    public abstract void add(@NonNull T item);

    @Override
    public void onUnexpectedError(Throwable e) {
        if (e instanceof DictionaryValidationException) {
            List<DictionaryValidationResult> results = ((DictionaryValidationException) e).getValidationResults();
            if (results.isEmpty()) {
                logger.error("validation results empty");
                return;
            }

            String msg = Observable.fromIterable(results)
                    .filter(DictionaryValidationResult::notValid)
                    .map(DictionaryValidationResult::toString)
                    .blockingFirst();
            getViewState().showValidationErrorMessage(msg);
        } else {
            super.onUnexpectedError(e);
        }
    }
}
