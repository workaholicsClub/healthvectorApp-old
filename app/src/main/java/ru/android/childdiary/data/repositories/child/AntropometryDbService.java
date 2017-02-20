package ru.android.childdiary.data.repositories.child;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
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
        // TODO: make reactive
        return dataStore.select(AntropometryEntity.class)
                .where(AntropometryEntity.CHILD_ID.eq(child.getId()))
                .orderBy(AntropometryEntity.DATE)
                .get().observable()
                .map(AntropometryMapper::map)
                .toList()
                .toObservable();
    }

    @Override
    public Observable<Antropometry> add(Antropometry antropometry) {
        // TODO: обобщить
        return Observable.fromCallable(() -> {
            BlockingEntityStore blockingEntityStore = dataStore.toBlocking();
            ChildEntity childEntity = (ChildEntity) blockingEntityStore.findByKey(ChildEntity.class, antropometry.getChild().getId());
            if (childEntity != null) {
                AntropometryEntity antropometryEntity = AntropometryMapper.map(antropometry);
                antropometryEntity.setChild(childEntity);
                antropometryEntity = (AntropometryEntity) blockingEntityStore.insert(antropometryEntity);
                return AntropometryMapper.map(antropometryEntity);
            }
            throw new RuntimeException("child not found while inserting antropometry");
        });
    }

    @Override
    public Observable<Antropometry> update(Antropometry antropometry) {
        return DbUtils.updateObservable(dataStore, AntropometryEntity.class, antropometry, antropometry.getId(),
                AntropometryMapper::copy, AntropometryMapper::map);
    }

    @Override
    public Observable<Antropometry> delete(Antropometry antropometry) {
        return DbUtils.deleteObservable(dataStore, AntropometryEntity.class, antropometry, antropometry.getId());
    }
}
