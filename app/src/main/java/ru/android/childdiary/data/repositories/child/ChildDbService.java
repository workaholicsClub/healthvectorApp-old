package ru.android.childdiary.data.repositories.child;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.domain.interactors.child.Child;

@Singleton
public class ChildDbService implements ChildService {
    private final ReactiveEntityStore<Persistable> dataStore;

    @Inject
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
        // delete object by id
        return Observable.create(emitter -> {
            BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
            ChildEntity result = (ChildEntity) blockingEntityStore.runInTransaction(() -> {
                Object childEntity = blockingEntityStore.findByKey(ChildEntity.class, child.getId());
                if (childEntity != null) {
                    blockingEntityStore.delete(childEntity);
                }
                return childEntity;
            });
            if (result == null) {
                throw new RuntimeException("child not found while deleting");
            }
            emitter.onNext(child);
            emitter.onComplete();
        });
    }
}
