package ru.android.healthvector.data.repositories.dictionaries.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public abstract class BaseCrudDbService<T> implements CrudDbService<T> {
    protected final Logger logger = LoggerFactory.getLogger(toString());
    protected final ReactiveEntityStore<Persistable> dataStore;
    protected final BlockingEntityStore<Persistable> blockingEntityStore;

    protected BaseCrudDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
    }
}
