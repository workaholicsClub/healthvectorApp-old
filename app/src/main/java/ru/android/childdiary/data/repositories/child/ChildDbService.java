package ru.android.childdiary.data.repositories.child;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.domain.interactors.child.Child;

@Singleton
public class ChildDbService implements ChildService {
    private final ReactiveEntityStore<Persistable> dataStore;

    public ChildDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public Observable<List<Child>> getAll() {
        return dataStore.select(ChildEntity.class)
                .orderBy(ChildEntity.NAME)
                .get().observable()
                .map(Mapper::map)
                .toList()
                .toObservable();
    }

    @Override
    public Observable<Child> add(Child child) {
        return dataStore.insert(Mapper.map(child)).toObservable().map(Mapper::map);
    }

    @Override
    public Observable<Child> update(Child child) {
        return dataStore.update(Mapper.map(child)).toObservable().map(Mapper::map);
    }

    @Override
    public Observable<Child> delete(Child child) {
        return dataStore.delete(Mapper.map(child)).toObservable();
    }
}
