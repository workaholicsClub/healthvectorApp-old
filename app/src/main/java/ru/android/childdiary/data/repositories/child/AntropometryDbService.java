package ru.android.childdiary.data.repositories.child;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Single;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.entities.child.AntropometryEntity;
import ru.android.childdiary.domain.models.child.Antropometry;
import ru.android.childdiary.domain.models.child.Child;

@Singleton
public class AntropometryDbService implements AntropometryService {
    private final ReactiveEntityStore<Persistable> dataStore;

    public AntropometryDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public Single<List<Antropometry>> getAll() {
        return dataStore.select(AntropometryEntity.class).get().observable()
                .map(Mapper::map)
                .toSortedList((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
    }

    @Override
    public Single<List<Antropometry>> getAll(Child child) {
        return dataStore.select(AntropometryEntity.class)
                .where(AntropometryEntity.CHILD_ID.eq(child.getId()))
                .get().observable()
                .map(Mapper::map)
                .toSortedList((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
    }

    @Override
    public Single<Antropometry> add(Antropometry antropometry) {
        return dataStore.insert(Mapper.map(antropometry)).map(Mapper::map);
    }

    @Override
    public Single<Antropometry> update(Antropometry antropometry) {
        return dataStore.update(Mapper.map(antropometry)).map(Mapper::map);
    }

    @Override
    public Single<Antropometry> delete(Antropometry antropometry) {
        return Single.fromObservable(dataStore.delete(Mapper.map(antropometry)).toObservable());
    }
}
