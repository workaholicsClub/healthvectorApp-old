package ru.android.childdiary.data.repositories.child;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.entities.child.AntropometryEntity;
import ru.android.childdiary.data.entities.child.ChildEntity;
import ru.android.childdiary.domain.interactors.child.Antropometry;
import ru.android.childdiary.domain.interactors.child.Child;

@Singleton
public class AntropometryDbService implements AntropometryService {
    private final ReactiveEntityStore<Persistable> dataStore;

    @Inject
    public AntropometryDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public Observable<List<Antropometry>> getAll(Child child) {
        return dataStore.select(AntropometryEntity.class)
                .where(AntropometryEntity.CHILD_ID.eq(child.getId()))
                .orderBy(AntropometryEntity.DATE)
                .get().observable()
                .map(Mapper::map)
                .toList()
                .toObservable();
    }

    @Override
    public Observable<Antropometry> add(Antropometry antropometry) {
        // insert object with foreign key
        return Observable.create(emitter -> {
            BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
            AntropometryEntity antropometryEntity = Mapper.map(antropometry);
            AntropometryEntity result = (AntropometryEntity) blockingEntityStore.runInTransaction(() -> {
                ChildEntity childEntity = (ChildEntity) blockingEntityStore.findByKey(ChildEntity.class, antropometry.getChild().getId());
                if (childEntity != null) {
                    antropometryEntity.setChild(childEntity);
                    return blockingEntityStore.insert(antropometryEntity);
                }
                return null;
            });
            if (result == null) {
                throw new RuntimeException("child not found while inserting antropometry");
            }
            emitter.onNext(Mapper.map(result));
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Antropometry> update(Antropometry antropometry) {
        return dataStore.update(Mapper.map(antropometry)).toObservable().map(Mapper::map);
    }

    @Override
    public Observable<Antropometry> delete(Antropometry antropometry) {
        // delete object by id
        return Observable.create(emitter -> {
            BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
            AntropometryEntity result = (AntropometryEntity) blockingEntityStore.runInTransaction(() -> {
                Object antropometryEntity = blockingEntityStore.findByKey(AntropometryEntity.class, antropometry.getId());
                if (antropometryEntity != null) {
                    blockingEntityStore.delete(antropometryEntity);
                }
                return antropometryEntity;
            });
            if (result == null) {
                throw new RuntimeException("antropometry not found while deleting");
            }
            emitter.onNext(antropometry);
            emitter.onComplete();
        });
    }
}
