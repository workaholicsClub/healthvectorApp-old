package ru.android.healthvector.data.repositories.dictionaries.food;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.healthvector.data.db.DbUtils;
import ru.android.healthvector.data.db.entities.dictionaries.FoodEntity;
import ru.android.healthvector.data.repositories.dictionaries.core.BaseCrudDbService;
import ru.android.healthvector.data.repositories.dictionaries.food.mappers.FoodMapper;
import ru.android.healthvector.domain.dictionaries.food.data.Food;

@Singleton
public class FoodDbService extends BaseCrudDbService<Food> {
    private final FoodMapper foodMapper;

    @Inject
    public FoodDbService(ReactiveEntityStore<Persistable> dataStore,
                         FoodMapper foodMapper) {
        super(dataStore);
        this.foodMapper = foodMapper;
    }

    @Override
    public Observable<List<Food>> getAll() {
        return dataStore.select(FoodEntity.class)
                .orderBy(FoodEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, foodMapper));
    }

    @Override
    public Observable<Food> add(@NonNull Food food) {
        return DbUtils.insertObservable(blockingEntityStore, food, foodMapper);
    }

    @Override
    public Observable<Food> update(@NonNull Food food) {
        return DbUtils.updateObservable(blockingEntityStore, food, foodMapper);
    }

    @Override
    public Observable<Food> delete(@NonNull Food food) {
        return DbUtils.deleteObservable(blockingEntityStore, FoodEntity.class, food, food.getId());
    }
}
