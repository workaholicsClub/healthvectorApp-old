package ru.android.childdiary.data.repositories.medical;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import ru.android.childdiary.data.repositories.core.MedicineTakingEventsGenerator;
import ru.android.childdiary.data.repositories.medical.mappers.MedicineMapper;
import ru.android.childdiary.data.repositories.medical.mappers.MedicineMeasureMapper;
import ru.android.childdiary.data.repositories.medical.mappers.MedicineTakingMapper;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.requests.GetMedicineTakingListRequest;
import ru.android.childdiary.domain.interactors.medical.requests.GetMedicineTakingListResponse;
import ru.android.childdiary.utils.ObjectUtils;

@Singleton
public class MedicineTakingDbService {
    private final Logger logger = LoggerFactory.getLogger(toString());
    private final ReactiveEntityStore<Persistable> dataStore;
    private final MedicineTakingEventsGenerator eventsGenerator;
    private final MedicineMapper medicineMapper;
    private final MedicineMeasureMapper medicineMeasureMapper;
    private final MedicineTakingMapper medicineTakingMapper;
    private final RepeatParametersMapper repeatParametersMapper;

    @Inject
    public MedicineTakingDbService(ReactiveEntityStore<Persistable> dataStore,
                                   MedicineTakingEventsGenerator eventsGenerator,
                                   MedicineMapper medicineMapper,
                                   MedicineMeasureMapper medicineMeasureMapper,
                                   MedicineTakingMapper medicineTakingMapper,
                                   RepeatParametersMapper repeatParametersMapper) {
        this.dataStore = dataStore;
        this.eventsGenerator = eventsGenerator;
        this.medicineMapper = medicineMapper;
        this.medicineMeasureMapper = medicineMeasureMapper;
        this.medicineTakingMapper = medicineTakingMapper;
        this.repeatParametersMapper = repeatParametersMapper;
    }

    public Observable<List<Medicine>> getMedicines() {
        return dataStore.select(MedicineEntity.class)
                .orderBy(MedicineEntity.NAME, MedicineEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, medicineMapper));
    }

    public Observable<Medicine> addMedicine(@NonNull Medicine medicine) {
        return DbUtils.insertObservable(dataStore, medicine, medicineMapper);
    }

    public Observable<Medicine> deleteMedicine(@NonNull Medicine medicine) {
        return DbUtils.deleteObservable(dataStore, MedicineEntity.class, medicine, medicine.getId());
    }

    public Observable<List<MedicineMeasure>> getMedicineMeasureList() {
        return dataStore.select(MedicineMeasureEntity.class)
                .orderBy(MedicineMeasureEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, medicineMeasureMapper));
    }

    public Observable<GetMedicineTakingListResponse> getMedicineTakingList(@NonNull GetMedicineTakingListRequest request) {
        Child child = request.getChild();
        return dataStore.select(MedicineTakingEntity.class)
                .where(MedicineTakingEntity.CHILD_ID.eq(child.getId()))
                .and(MedicineTakingEntity.DELETED.isNull().or(MedicineTakingEntity.DELETED.eq(false)))
                .orderBy(MedicineTakingEntity.DATE_TIME, MedicineTakingEntity.MEDICINE_ID, MedicineTakingEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, medicineTakingMapper))
                .map(medicineTakingList -> GetMedicineTakingListResponse.builder().request(request).medicineTakingList(medicineTakingList).build());
    }

    private RepeatParameters insertRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.insert(dataStore, repeatParameters, repeatParametersMapper);
    }

    private MedicineTaking insertMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return DbUtils.insert(dataStore, medicineTaking, medicineTakingMapper);
    }

    private RepeatParameters updateRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.update(dataStore, repeatParameters, repeatParametersMapper);
    }

    private MedicineTaking updateMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return DbUtils.update(dataStore, medicineTaking, medicineTakingMapper);
    }

    @Nullable
    private RepeatParameters upsertRepeatParameters(@Nullable RepeatParameters repeatParameters) {
        if (repeatParameters != null) {
            if (repeatParameters.getId() == null) {
                return insertRepeatParameters(repeatParameters);
            } else {
                return updateRepeatParameters(repeatParameters);
            }
        }
        return null;
    }

    public Observable<MedicineTaking> add(@NonNull MedicineTaking medicineTaking) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            RepeatParameters repeatParameters = upsertRepeatParameters(medicineTaking.getRepeatParameters());
            MedicineTaking result = medicineTaking.toBuilder().repeatParameters(repeatParameters).build();
            result = insertMedicineTaking(result);

            boolean needToAddEvents = ObjectUtils.isTrue(result.getIsExported());

            if (needToAddEvents) {
                eventsGenerator.generateEvents(result);
            }

            return result;
        }));
    }

    public Observable<MedicineTaking> update(@NonNull MedicineTaking medicineTaking) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            MedicineTakingEntity oldMedicineTakingEntity = dataStore.toBlocking()
                    .select(MedicineTakingEntity.class)
                    .where(MedicineTakingEntity.ID.eq(medicineTaking.getId()))
                    .get()
                    .first();

            boolean needToAddEvents = ObjectUtils.isFalse(oldMedicineTakingEntity.isExported())
                    && ObjectUtils.isTrue(medicineTaking.getIsExported());

            RepeatParameters repeatParameters = upsertRepeatParameters(medicineTaking.getRepeatParameters());
            MedicineTaking result = medicineTaking.toBuilder().repeatParameters(repeatParameters).build();
            result = updateMedicineTaking(result);

            if (needToAddEvents) {
                eventsGenerator.generateEvents(result);
            }

            return result;
        }));
    }


}
