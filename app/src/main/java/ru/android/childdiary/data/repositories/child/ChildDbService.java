package ru.android.childdiary.data.repositories.child;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.db.entities.child.ChildEntity;
import ru.android.childdiary.data.repositories.child.mappers.ChildMapper;
import ru.android.childdiary.domain.interactors.child.Child;

@Singleton
public class ChildDbService {
    private final ReactiveEntityStore<Persistable> dataStore;
    private final BlockingEntityStore<Persistable> blockingEntityStore;
    private final ChildMapper childMapper;

    @Inject
    public ChildDbService(ReactiveEntityStore<Persistable> dataStore,
                          ChildMapper childMapper) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
        this.childMapper = childMapper;
    }

    public Observable<List<Child>> getAll() {
        return dataStore.select(ChildEntity.class)
                .orderBy(ChildEntity.NAME, ChildEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, childMapper));
    }

    public Observable<Child> add(@NonNull Child child) {
        return DbUtils.insertObservable(blockingEntityStore, child, childMapper);
    }

    public Observable<Child> update(@NonNull Child child) {
        return DbUtils.updateObservable(blockingEntityStore, child, childMapper);
    }
}
