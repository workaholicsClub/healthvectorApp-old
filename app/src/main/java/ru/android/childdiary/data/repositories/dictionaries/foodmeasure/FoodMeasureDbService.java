package ru.android.childdiary.data.repositories.dictionaries.foodmeasure;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.db.entities.dictionaries.FoodMeasureEntity;
import ru.android.childdiary.data.repositories.dictionaries.core.BaseCrudDbService;
import ru.android.childdiary.data.repositories.dictionaries.foodmeasure.mappers.FoodMeasureMapper;
import ru.android.childdiary.domain.dictionaries.foodmeasure.data.FoodMeasure;

@Singleton
public class FoodMeasureDbService extends BaseCrudDbService<FoodMeasure> {
    private final FoodMeasureMapper foodMeasureMapper;

    @Inject
    public FoodMeasureDbService(ReactiveEntityStore<Persistable> dataStore,
                                FoodMeasureMapper foodMeasureMapper) {
        super(dataStore);
        this.foodMeasureMapper = foodMeasureMapper;
    }

    @Override
    public Observable<List<FoodMeasure>> getAll() {
        return dataStore.select(FoodMeasureEntity.class)
                .orderBy(FoodMeasureEntity.NAME, FoodMeasureEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, foodMeasureMapper));
    }

    @Override
    public Observable<FoodMeasure> add(@NonNull FoodMeasure foodMeasure) {
        return DbUtils.insertObservable(blockingEntityStore, foodMeasure, foodMeasureMapper);
    }

    @Override
    public Observable<FoodMeasure> update(@NonNull FoodMeasure foodMeasure) {
        return DbUtils.updateObservable(blockingEntityStore, foodMeasure, foodMeasureMapper);
    }

    @Override
    public Observable<FoodMeasure> delete(@NonNull FoodMeasure foodMeasure) {
        return DbUtils.deleteObservable(blockingEntityStore, FoodMeasureEntity.class, foodMeasure, foodMeasure.getId());
    }
}
