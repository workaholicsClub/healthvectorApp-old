package ru.android.childdiary.data.repositories.child;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildRepository;

@Singleton
public class ChildDbService {
    private final ReactiveEntityStore<Persistable> dataStore;

    @Inject
    public ChildDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    public Observable<List<Child>> getAll() {
        return dataStore.select(ChildEntity.class)
                .orderBy(ChildEntity.NAME)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, ChildMapper::mapToPlainObject));
    }

    public Observable<Child> add(@NonNull Child child) {
        return dataStore.insert(ChildMapper.mapToEntity(child)).toObservable().map(ChildMapper::mapToPlainObject);
    }

    public Observable<Child> update(@NonNull Child child) {
        return DbUtils.updateObservable(dataStore, ChildEntity.class, child, child.getId(),
                ChildMapper::updateEntityWithPlainObject, ChildMapper::mapToPlainObject);
    }

    public Observable<Child> delete(@NonNull Child child) {
        return DbUtils.deleteObservable(dataStore, ChildEntity.class, child, child.getId());
    }
}
