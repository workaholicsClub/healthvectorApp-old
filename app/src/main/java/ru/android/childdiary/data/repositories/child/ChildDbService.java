package ru.android.childdiary.data.repositories.child;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Single;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.domain.models.child.Child;

@Singleton
public class ChildDbService implements ChildService {
    private final ReactiveEntityStore<Persistable> dataStore;

    public ChildDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public Single<List<Child>> getAll() {
        return dataStore.select(ChildEntity.class).get().observable()
                .map(Mapper::map)
                .toSortedList((o1, o2) -> o1.getName().compareTo(o2.getName()));
    }

    @Override
    public Single<Child> add(Child child) {
        return dataStore.insert(Mapper.map(child)).map(Mapper::map);
    }

    @Override
    public Single<Child> update(Child child) {
        return dataStore.update(Mapper.map(child)).map(Mapper::map);
    }

    @Override
    public Single<Child> delete(Child child) {
        return Single.fromObservable(dataStore.delete(Mapper.map(child)).toObservable());
    }
}
