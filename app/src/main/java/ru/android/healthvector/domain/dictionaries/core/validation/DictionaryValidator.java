package ru.android.healthvector.domain.dictionaries.core.validation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ru.android.healthvector.R;
import ru.android.healthvector.domain.core.validation.core.ValidationException;
import ru.android.healthvector.domain.core.validation.core.Validator;
import ru.android.healthvector.domain.dictionaries.core.CrudRepository;
import ru.android.healthvector.utils.ObjectUtils;

public abstract class DictionaryValidator<T> extends Validator<T, DictionaryValidationResult> {
    protected final Context context;
    protected final CrudRepository<T> repository;

    protected DictionaryValidator(Context context,
                                  CrudRepository<T> repository) {
        this.context = context;
        this.repository = repository;
    }

    @Override
    public List<DictionaryValidationResult> validate(@NonNull T item) {
        List<DictionaryValidationResult> results = new ArrayList<>();

        DictionaryValidationResult result;

        if (TextUtils.isEmpty(getName(item))) {
            result = new DictionaryValidationResult(getNameFieldType());
            result.addMessage(getEmptyNameMessage());
            results.add(result);
        } else {
            result = new DictionaryValidationResult(getNameFieldType());
            results.add(result);

            long count = Observable.fromIterable(repository.getAll().blockingFirst())
                    .filter(item1 -> same(item1, item))
                    .count()
                    .blockingGet();

            if (count > 0) {
                result = new DictionaryValidationResult(null);
                result.addMessage(context.getString(R.string.the_value_already_exists));
                results.add(result);
            }
        }

        return results;
    }

    @Override
    public List<DictionaryValidationResult> validateOnUi(@NonNull T item) {
        List<DictionaryValidationResult> results = new ArrayList<>();

        DictionaryValidationResult result = new DictionaryValidationResult(getNameFieldType());
        if (TextUtils.isEmpty(getName(item))) {
            result.addMessage(getEmptyNameMessage());
        }
        results.add(result);

        return results;
    }

    @Override
    protected ValidationException createException(@NonNull List<DictionaryValidationResult> results) {
        return new DictionaryValidationException(results);
    }

    protected abstract String getName(@NonNull T item);

    protected abstract DictionaryFieldType getNameFieldType();

    protected abstract String getEmptyNameMessage();

    protected boolean same(@NonNull T item1, @NonNull T item2) {
        return ObjectUtils.contentEquals(getName(item1), getName(item2));
    }
}
