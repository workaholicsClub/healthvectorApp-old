package ru.android.childdiary.data.repositories.medical;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.medical.core.MedicineEntity;
import ru.android.childdiary.data.repositories.medical.mappers.MedicineMapper;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

@Singleton
public class MedicineTakingDbService {
    private final ReactiveEntityStore<Persistable> dataStore;

    @Inject
    public MedicineTakingDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    public Observable<List<Medicine>> getMedicines() {
        return dataStore.select(MedicineEntity.class)
                .orderBy(MedicineEntity.NAME, MedicineEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, MedicineMapper::mapToPlainObject));
    }

    public Observable<Medicine> addMedicine(@NonNull Medicine medicine) {
        return null;
    }

    public Observable<List<MedicineTaking>> getMedicineTakingList() {
        return null;
    }

    public Observable<Medicine> addMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return null;
    }
}
