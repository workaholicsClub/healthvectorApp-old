package ru.android.childdiary.domain.dictionaries.core;

import android.support.annotation.NonNull;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.dictionaries.core.validation.DictionaryValidationResult;

public interface DictionaryInteractor<T> {
    Observable<List<T>> getAll();

    Observable<T> add(@NonNull T item);

    Observable<T> update(@NonNull T item);

    Observable<T> delete(@NonNull T item);

    Observable<Boolean> controlDoneButton(@NonNull Observable<TextViewAfterTextChangeEvent> nameObservable);

    Observable<List<DictionaryValidationResult>> controlFields(@NonNull Observable<TextViewAfterTextChangeEvent> nameObservable);
}
