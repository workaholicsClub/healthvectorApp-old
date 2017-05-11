package ru.android.childdiary.data.repositories.child;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.child.AntropometryEntity;
import ru.android.childdiary.data.repositories.child.mappers.AntropometryMapper;
import ru.android.childdiary.domain.interactors.child.Antropometry;
import ru.android.childdiary.domain.interactors.child.Child;

@Singleton
public class AntropometryDbService {
    private final ReactiveEntityStore<Persistable> dataStore;

    @Inject
    public AntropometryDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    public Observable<List<Antropometry>> getAll(@NonNull Child child) {
        return dataStore.select(AntropometryEntity.class)
                .where(AntropometryEntity.CHILD_ID.eq(child.getId()))
                .orderBy(AntropometryEntity.DATE, AntropometryEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, AntropometryMapper::mapToPlainObject));
    }

    public Observable<Antropometry> add(@NonNull Antropometry antropometry) {
        return DbUtils.insertObservable(dataStore, antropometry,
                AntropometryMapper::mapToEntity, AntropometryMapper::mapToPlainObject);
    }

    public Observable<Antropometry> update(@NonNull Antropometry antropometry) {
        return DbUtils.updateObservable(dataStore, antropometry,
                AntropometryMapper::mapToEntity, AntropometryMapper::mapToPlainObject);
    }

    public Observable<Antropometry> delete(@NonNull Antropometry antropometry) {
        return DbUtils.deleteObservable(dataStore, AntropometryEntity.class, antropometry, antropometry.getId());
    }
}
