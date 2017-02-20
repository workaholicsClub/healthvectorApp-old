package ru.android.childdiary.data.repositories.child;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveResult;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.domain.interactors.child.Child;

@Singleton
public class ChildDbService implements ChildService {
    private final ReactiveEntityStore<Persistable> dataStore;

    @Inject
    public ChildDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    private static Observable<List<Child>> mapReactiveResult(ReactiveResult<ChildEntity> reactiveResult) {
        return reactiveResult
                .observable()
                .map(ChildMapper::map)
                .toList()
                .toObservable();
    }

    @Override
    public Observable<List<Child>> getAll() {
        return dataStore.select(ChildEntity.class)
                .orderBy(ChildEntity.NAME)
                .get()
                .observableResult()
                .flatMap(ChildDbService::mapReactiveResult);
    }

    @Override
    public Observable<Child> add(Child child) {
        return dataStore.insert(ChildMapper.map(child)).toObservable().map(ChildMapper::map);
    }

    @Override
    public Observable<Child> update(Child child) {
        return DbUtils.updateObservable(dataStore, ChildEntity.class, child, child.getId(),
                ChildMapper::copy, ChildMapper::map);
    }

    @Override
    public Observable<Child> delete(Child child) {
        return DbUtils.deleteObservable(dataStore, ChildEntity.class, child, child.getId());
    }
}
