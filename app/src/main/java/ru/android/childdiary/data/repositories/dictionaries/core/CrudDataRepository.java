package ru.android.childdiary.data.repositories.dictionaries.core;

import android.support.annotation.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.dictionaries.core.CrudRepository;
import ru.android.childdiary.domain.dictionaries.core.DictionaryItem;

public abstract class CrudDataRepository<T extends DictionaryItem> implements CrudRepository<T> {
    protected final Logger logger = LoggerFactory.getLogger(toString());
    protected final CrudDbService<T> dbService;

    protected CrudDataRepository(CrudDbService<T> dbService) {
        this.dbService = dbService;
    }

    @Override
    public Observable<List<T>> getAll() {
        return dbService.getAll()
                .map(this::sort);
    }

    private List<T> sort(@NonNull List<T> list) {
        Collections.sort(list, (item1, item2) -> DictionaryItem.getLocalizedName(item1)
                .compareTo(DictionaryItem.getLocalizedName(item2)));
        return list;
    }

    @Override
    public Observable<T> add(@NonNull T item) {
        return dbService.add(item);
    }

    @Override
    public Observable<T> update(@NonNull T item) {
        return dbService.update(item);
    }

    @Override
    public Observable<T> delete(@NonNull T item) {
        return dbService.delete(item);
    }
}
