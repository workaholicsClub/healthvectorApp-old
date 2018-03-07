package ru.android.healthvector.domain.dictionaries.core;

import android.support.annotation.NonNull;
import android.text.Editable;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.reactivex.Observable;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryValidationResult;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryValidator;

public abstract class BaseDictionaryInteractor<T> implements DictionaryInteractor<T> {
    protected final Logger logger = LoggerFactory.getLogger(toString());
    protected final CrudRepository<T> repository;
    protected final DictionaryValidator<T> validator;

    protected BaseDictionaryInteractor(CrudRepository<T> repository,
                                       DictionaryValidator<T> validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public Observable<List<T>> getAll() {
        return repository.getAll();
    }

    @Override
    public Observable<T> add(@NonNull T item) {
        return validator.validateObservable(item)
                .flatMap(repository::add);
    }

    @Override
    public Observable<T> update(@NonNull T item) {
        return validator.validateObservable(item)
                .flatMap(repository::update);
    }

    @Override
    public Observable<T> delete(@NonNull T item) {
        return repository.delete(item);
    }

    @Override
    public Observable<Boolean> controlDoneButton(@NonNull Observable<TextViewAfterTextChangeEvent> nameObservable) {
        return nameObservable
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Editable::toString)
                .map(String::trim)
                .map(this::buildItem)
                .map(validator::validateOnUi)
                .map(validator::isValid)
                .distinctUntilChanged();
    }

    @Override
    public Observable<List<DictionaryValidationResult>> controlFields(@NonNull Observable<TextViewAfterTextChangeEvent> nameObservable) {
        return nameObservable
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Editable::toString)
                .map(String::trim)
                .map(this::buildItem)
                .map(validator::validateOnUi);
    }

    protected abstract T buildItem(String name);
}
