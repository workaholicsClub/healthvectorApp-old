package ru.android.childdiary.data.repositories.dictionaries.medicinemeasure;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.db.entities.dictionaries.MedicineMeasureEntity;
import ru.android.childdiary.data.repositories.dictionaries.core.BaseCrudDbService;
import ru.android.childdiary.data.repositories.dictionaries.medicinemeasure.mappers.MedicineMeasureMapper;
import ru.android.childdiary.domain.interactors.dictionaries.medicinemeasure.data.MedicineMeasure;

@Singleton
public class MedicineMeasureDbService extends BaseCrudDbService<MedicineMeasure> {
    private final MedicineMeasureMapper medicineMeasureMapper;

    @Inject
    public MedicineMeasureDbService(ReactiveEntityStore<Persistable> dataStore,
                                    MedicineMeasureMapper medicineMeasureMapper) {
        super(dataStore);
        this.medicineMeasureMapper = medicineMeasureMapper;
    }

    @Override
    public Observable<List<MedicineMeasure>> getAll() {
        return dataStore.select(MedicineMeasureEntity.class)
                .orderBy(MedicineMeasureEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, medicineMeasureMapper));
    }

    @Override
    public Observable<MedicineMeasure> add(@NonNull MedicineMeasure medicineMeasure) {
        return DbUtils.insertObservable(blockingEntityStore, medicineMeasure, medicineMeasureMapper);
    }

    @Override
    public Observable<MedicineMeasure> update(@NonNull MedicineMeasure medicineMeasure) {
        return DbUtils.updateObservable(blockingEntityStore, medicineMeasure, medicineMeasureMapper);
    }

    @Override
    public Observable<MedicineMeasure> delete(@NonNull MedicineMeasure medicineMeasure) {
        return DbUtils.deleteObservable(blockingEntityStore, MedicineMeasureEntity.class, medicineMeasure, medicineMeasure.getId());
    }
}
