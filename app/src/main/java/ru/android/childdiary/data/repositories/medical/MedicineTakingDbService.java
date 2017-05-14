package ru.android.childdiary.data.repositories.medical;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import lombok.val;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.calendar.events.MedicineTakingEventEntity;
import ru.android.childdiary.data.entities.calendar.events.core.MasterEventEntity;
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
import ru.android.childdiary.domain.interactors.medical.requests.MedicineTakingListRequest;
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

    public Observable<List<MedicineTaking>> getMedicineTakingList(@NonNull MedicineTakingListRequest request) {
        Child child = request.getChild();
        return dataStore.select(MedicineTakingEntity.class)
                .where(MedicineTakingEntity.CHILD_ID.eq(child.getId()))
                .and(MedicineTakingEntity.DELETED.isNull().or(MedicineTakingEntity.DELETED.eq(false)))
                .orderBy(MedicineTakingEntity.DATE_TIME, MedicineTakingEntity.MEDICINE_ID, MedicineTakingEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, medicineTakingMapper));
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

    public Observable<Integer> deleteMedicineTakingEvents(@NonNull Long medicineTakingId,
                                                          @Nullable Integer linearGroup) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            BlockingEntityStore<Persistable> blockingEntityStore = dataStore.toBlocking();
            val where = blockingEntityStore
                    .select(MasterEventEntity.class)
                    .join(MedicineTakingEventEntity.class).on(MasterEventEntity.ID.eq(MedicineTakingEventEntity.MASTER_EVENT_ID))
                    .where(MedicineTakingEventEntity.MEDICINE_TAKING_ID.eq(medicineTakingId));
            MedicineTakingEntity medicineTakingEntity = blockingEntityStore
                    .findByKey(MedicineTakingEntity.class, medicineTakingId);
            if (linearGroup == null) {
                List<MasterEventEntity> events = where.get().toList();
                int count = events.size();
                deleteManyEvents(blockingEntityStore, events);
                blockingEntityStore.delete(medicineTakingEntity);
                return count;
            } else {
                List<MasterEventEntity> events = where.and(MasterEventEntity.LINEAR_GROUP.eq(linearGroup)).get().toList();
                int count = events.size();
                deleteManyEvents(blockingEntityStore, events);
                deleteIfPossible(blockingEntityStore, medicineTakingEntity);
                return count;
            }
        }));
    }

    public Observable<Integer> completeMedicineTaking(@NonNull Long medicineTakingId,
                                                      @NonNull DateTime dateTime,
                                                      boolean delete) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            BlockingEntityStore<Persistable> blockingEntityStore = dataStore.toBlocking();
            MedicineTakingEntity medicineTakingEntity = blockingEntityStore
                    .findByKey(MedicineTakingEntity.class, medicineTakingId);
            medicineTakingEntity.setFinishDateTime(dateTime);
            blockingEntityStore.update(medicineTakingEntity);
            int count = 0;
            if (delete) {
                List<MasterEventEntity> events = blockingEntityStore
                        .select(MasterEventEntity.class)
                        .join(MedicineTakingEventEntity.class).on(MasterEventEntity.ID.eq(MedicineTakingEventEntity.MASTER_EVENT_ID))
                        .where(MedicineTakingEventEntity.MEDICINE_TAKING_ID.eq(medicineTakingId))
                        .and(MasterEventEntity.DATE_TIME.greaterThanOrEqual(dateTime.toDateTime()))
                        .get()
                        .toList();
                count = events.size();
                deleteManyEvents(blockingEntityStore, events);
            }
            return count;
        }));
    }

    public Observable<MedicineTaking> delete(@NonNull MedicineTaking medicineTaking) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            BlockingEntityStore<Persistable> blockingEntityStore = dataStore.toBlocking();
            List<MasterEventEntity> events = blockingEntityStore
                    .select(MasterEventEntity.class)
                    .join(MedicineTakingEventEntity.class).on(MasterEventEntity.ID.eq(MedicineTakingEventEntity.MASTER_EVENT_ID))
                    .where(MedicineTakingEventEntity.MEDICINE_TAKING_ID.eq(medicineTaking.getId()))
                    .get()
                    .toList();
            int count = events.size();
            MedicineTakingEntity medicineTakingEntity = blockingEntityStore
                    .findByKey(MedicineTakingEntity.class, medicineTaking.getId());
            if (count == 0) {
                blockingEntityStore.delete(medicineTakingEntity);
                logger.debug("medicine taking deleted hardly");
            } else {
                medicineTakingEntity.setDeleted(true);
                blockingEntityStore.update(medicineTakingEntity);
                logger.debug("medicine taking deleted softly");
            }
            return medicineTakingMapper.mapToPlainObject(medicineTakingEntity);
        }));
    }

    private void deleteIfPossible(BlockingEntityStore<Persistable> blockingEntityStore,
                                  MedicineTakingEntity medicineTakingEntity) {
        if (ObjectUtils.isTrue(medicineTakingEntity.isDeleted())) {
            List<MasterEventEntity> events = blockingEntityStore
                    .select(MasterEventEntity.class)
                    .join(MedicineTakingEventEntity.class).on(MasterEventEntity.ID.eq(MedicineTakingEventEntity.MASTER_EVENT_ID))
                    .where(MedicineTakingEventEntity.MEDICINE_TAKING_ID.eq(medicineTakingEntity.getId()))
                    .get()
                    .toList();
            if (events.isEmpty()) {
                blockingEntityStore.delete(medicineTakingEntity);
                logger.debug("medicine taking deleted hardly");
            }
        }
    }

    private void deleteManyEvents(BlockingEntityStore<Persistable> blockingEntityStore,
                                  List<MasterEventEntity> events) {
        final int MAX = 10;
        for (int i = 0; i < events.size(); i += MAX) {
            int upper = Math.min(i + MAX, events.size());
            val subList = events.subList(i, upper);
            blockingEntityStore.delete(subList);
        }
    }
}
