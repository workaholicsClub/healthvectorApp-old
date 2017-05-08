package ru.android.childdiary.data.repositories.medical;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.medical.MedicineTakingEntity;
import ru.android.childdiary.data.entities.medical.core.MedicineEntity;
import ru.android.childdiary.data.entities.medical.core.MedicineMeasureEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.RepeatParametersMapper;
import ru.android.childdiary.data.repositories.medical.mappers.MedicineMapper;
import ru.android.childdiary.data.repositories.medical.mappers.MedicineMeasureMapper;
import ru.android.childdiary.data.repositories.medical.mappers.MedicineTakingMapper;
import ru.android.childdiary.domain.interactors.calendar.events.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.requests.MedicineTakingListRequest;

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
        return DbUtils.insertObservable(dataStore, medicine, MedicineMapper::mapToEntity, MedicineMapper::mapToPlainObject);
    }

    public Observable<Medicine> deleteMedicine(@NonNull Medicine medicine) {
        return DbUtils.deleteObservable(dataStore, MedicineEntity.class, medicine, medicine.getId());
    }

    public Observable<List<MedicineMeasure>> getMedicineMeasureList() {
        return dataStore.select(MedicineMeasureEntity.class)
                .orderBy(MedicineMeasureEntity.NAME, MedicineMeasureEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, MedicineMeasureMapper::mapToPlainObject));
    }

    public Observable<List<MedicineTaking>> getMedicineTakingList(@NonNull MedicineTakingListRequest request) {
        Child child = request.getChild();
        return dataStore.select(MedicineTakingEntity.class)
                .where(MedicineTakingEntity.CHILD_ID.eq(child.getId()))
                .orderBy(MedicineTakingEntity.DATE_TIME, MedicineTakingEntity.MEDICINE_ID, MedicineTakingEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, MedicineTakingMapper::mapToPlainObject));
    }

    private RepeatParameters insertRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.insert(dataStore, repeatParameters,
                RepeatParametersMapper::mapToEntity, RepeatParametersMapper::mapToPlainObject);
    }

    private MedicineTaking insertMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return DbUtils.insert(dataStore, medicineTaking,
                MedicineTakingMapper::mapToEntity, MedicineTakingMapper::mapToPlainObject);
    }

    public Observable<MedicineTaking> add(@NonNull MedicineTaking medicineTaking) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            MedicineTaking object = medicineTaking;
            RepeatParameters repeatParameters = object.getRepeatParameters();
            if (repeatParameters != null) {
                repeatParameters = insertRepeatParameters(repeatParameters);
                object = object.toBuilder().repeatParameters(repeatParameters).build();
            }
            return insertMedicineTaking(object);
        }));
    }

    private RepeatParameters updateRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.update(dataStore, repeatParameters,
                RepeatParametersMapper::mapToEntity, RepeatParametersMapper::mapToPlainObject);
    }

    private MedicineTaking updateMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return DbUtils.update(dataStore, medicineTaking,
                MedicineTakingMapper::mapToEntity, MedicineTakingMapper::mapToPlainObject);
    }

    public Observable<MedicineTaking> update(@NonNull MedicineTaking medicineTaking) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            MedicineTaking object = medicineTaking;
            RepeatParameters repeatParameters = object.getRepeatParameters();
            if (repeatParameters != null) {
                if (repeatParameters.getId() == null) {
                    repeatParameters = insertRepeatParameters(repeatParameters);
                } else {
                    repeatParameters = updateRepeatParameters(repeatParameters);
                }
                object = object.toBuilder().repeatParameters(repeatParameters).build();
            }
            return updateMedicineTaking(object);
        }));
    }
}
