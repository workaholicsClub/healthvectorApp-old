package ru.android.childdiary.data.repositories.development.antropometry;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.db.entities.development.antropometry.AntropometryEntity;
import ru.android.childdiary.data.repositories.development.antropometry.mappers.AntropometryMapper;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.development.antropometry.Antropometry;

@Singleton
public class AntropometryDbService {
    private final ReactiveEntityStore<Persistable> dataStore;
    private final BlockingEntityStore<Persistable> blockingEntityStore;
    private final AntropometryMapper antropometryMapper;

    @Inject
    public AntropometryDbService(ReactiveEntityStore<Persistable> dataStore,
                                 AntropometryMapper antropometryMapper) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
        this.antropometryMapper = antropometryMapper;
    }

    public Observable<List<Antropometry>> getAll(@NonNull Child child) {
        return dataStore.select(AntropometryEntity.class)
                .where(AntropometryEntity.CHILD_ID.eq(child.getId()))
                .orderBy(AntropometryEntity.DATE, AntropometryEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, antropometryMapper));
    }

    public Observable<Antropometry> add(@NonNull Antropometry antropometry) {
        return DbUtils.insertObservable(blockingEntityStore, antropometry, antropometryMapper);
    }

    public Observable<Antropometry> update(@NonNull Antropometry antropometry) {
        return DbUtils.updateObservable(blockingEntityStore, antropometry, antropometryMapper);
    }

    public Observable<Antropometry> delete(@NonNull Antropometry antropometry) {
        return DbUtils.deleteObservable(blockingEntityStore, AntropometryEntity.class, antropometry, antropometry.getId());
    }
}
