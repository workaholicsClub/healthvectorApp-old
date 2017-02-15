package ru.android.childdiary.data.repositories.child;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.entities.child.AntropometryEntity;
import ru.android.childdiary.domain.interactors.child.Antropometry;
import ru.android.childdiary.domain.interactors.child.Child;

@Singleton
public class AntropometryDbService implements AntropometryService {
    private final ReactiveEntityStore<Persistable> dataStore;

    public AntropometryDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public Observable<List<Antropometry>> getAll() {
        return dataStore.select(AntropometryEntity.class)
                .orderBy(AntropometryEntity.DATE)
                .get().observable()
                .map(Mapper::map)
                .toList()
                .toObservable();
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
        return dataStore.insert(Mapper.map(antropometry)).toObservable().map(Mapper::map);
    }

    @Override
    public Observable<Antropometry> update(Antropometry antropometry) {
        return dataStore.update(Mapper.map(antropometry)).toObservable().map(Mapper::map);
    }

    @Override
    public Observable<Antropometry> delete(Antropometry antropometry) {
        return dataStore.delete(Mapper.map(antropometry)).toObservable();
    }
}
