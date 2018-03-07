package ru.android.healthvector.data.repositories.dictionaries.medicines;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.healthvector.data.db.DbUtils;
import ru.android.healthvector.data.db.entities.dictionaries.MedicineEntity;
import ru.android.healthvector.data.repositories.dictionaries.core.BaseCrudDbService;
import ru.android.healthvector.data.repositories.dictionaries.medicines.mappers.MedicineMapper;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;

@Singleton
public class MedicineDbService extends BaseCrudDbService<Medicine> {
    private final MedicineMapper medicineMapper;

    @Inject
    public MedicineDbService(ReactiveEntityStore<Persistable> dataStore,
                             MedicineMapper medicineMapper) {
        super(dataStore);
        this.medicineMapper = medicineMapper;
    }

    @Override
    public Observable<List<Medicine>> getAll() {
        return dataStore.select(MedicineEntity.class)
                .orderBy(MedicineEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, medicineMapper));
    }

    @Override
    public Observable<Medicine> add(@NonNull Medicine medicine) {
        return DbUtils.insertObservable(blockingEntityStore, medicine, medicineMapper);
    }

    @Override
    public Observable<Medicine> update(@NonNull Medicine medicine) {
        return DbUtils.updateObservable(blockingEntityStore, medicine, medicineMapper);
    }

    @Override
    public Observable<Medicine> delete(@NonNull Medicine medicine) {
        return DbUtils.deleteObservable(blockingEntityStore, MedicineEntity.class, medicine, medicine.getId());
    }
}
