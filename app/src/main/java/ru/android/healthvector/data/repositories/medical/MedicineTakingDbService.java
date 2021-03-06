package ru.android.healthvector.data.repositories.medical;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.query.WhereAndOr;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveResult;
import lombok.val;
import ru.android.healthvector.data.db.DbUtils;
import ru.android.healthvector.data.db.entities.calendar.MedicineTakingEventEntity;
import ru.android.healthvector.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.healthvector.data.db.entities.medical.MedicineTakingEntity;
import ru.android.healthvector.data.repositories.calendar.mappers.RepeatParametersMapper;
import ru.android.healthvector.data.repositories.core.generators.EventsGenerator;
import ru.android.healthvector.data.repositories.core.generators.MedicineTakingEventsGenerator;
import ru.android.healthvector.data.repositories.medical.mappers.MedicineTakingMapper;
import ru.android.healthvector.domain.calendar.data.core.LengthValue;
import ru.android.healthvector.domain.calendar.data.core.RepeatParameters;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.domain.medical.data.MedicineTaking;
import ru.android.healthvector.domain.medical.requests.GetMedicineTakingListFilter;
import ru.android.healthvector.domain.medical.requests.GetMedicineTakingListRequest;
import ru.android.healthvector.domain.medical.requests.GetMedicineTakingListResponse;
import ru.android.healthvector.domain.medical.requests.UpsertMedicineTakingRequest;
import ru.android.healthvector.domain.medical.requests.UpsertMedicineTakingResponse;
import ru.android.healthvector.utils.ObjectUtils;
import ru.android.healthvector.utils.strings.DateUtils;

@Singleton
public class MedicineTakingDbService {
    private final Logger logger = LoggerFactory.getLogger(toString());
    private final ReactiveEntityStore<Persistable> dataStore;
    private final BlockingEntityStore<Persistable> blockingEntityStore;
    private final EventsGenerator<MedicineTaking> eventsGenerator;
    private final MedicineTakingMapper medicineTakingMapper;
    private final RepeatParametersMapper repeatParametersMapper;

    @Inject
    public MedicineTakingDbService(ReactiveEntityStore<Persistable> dataStore,
                                   MedicineTakingEventsGenerator eventsGenerator,
                                   MedicineTakingMapper medicineTakingMapper,
                                   RepeatParametersMapper repeatParametersMapper) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
        this.eventsGenerator = eventsGenerator;
        this.medicineTakingMapper = medicineTakingMapper;
        this.repeatParametersMapper = repeatParametersMapper;
    }

    public Observable<GetMedicineTakingListResponse> getMedicineTakingList(@NonNull GetMedicineTakingListRequest request) {
        Child child = request.getChild();
        WhereAndOr<ReactiveResult<MedicineTakingEntity>> select = dataStore.select(MedicineTakingEntity.class)
                .where(MedicineTakingEntity.CHILD_ID.eq(child.getId()))
                .and(MedicineTakingEntity.DELETED.isNull().or(MedicineTakingEntity.DELETED.eq(false)));

        GetMedicineTakingListFilter filter = request.getFilter();

        LocalDate fromDate = filter.getFromDate();
        if (fromDate != null) {
            select = select.and(MedicineTakingEntity.DATE_TIME.greaterThanOrEqual(DateUtils.midnight(fromDate)));
        }

        LocalDate toDate = filter.getToDate();
        if (toDate != null) {
            select = select.and(MedicineTakingEntity.DATE_TIME.lessThan(DateUtils.nextDayMidnight(toDate)));
        }

        List<Medicine> medicines = filter.getSelectedItems();
        if (!medicines.isEmpty()) {
            List<Long> ids = Observable.fromIterable(medicines)
                    .filter(medicine -> medicine.getId() != null)
                    .map(Medicine::getId)
                    .toList()
                    .blockingGet();
            select = select.and(MedicineTakingEntity.MEDICINE_ID.in(ids));
        }

        return select.orderBy(MedicineTakingEntity.DATE_TIME.desc(), MedicineTakingEntity.MEDICINE_ID, MedicineTakingEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, medicineTakingMapper))
                .map(medicineTakingList -> putDone(medicineTakingList, child))
                .map(this::sort)
                .map(medicineTakingList -> GetMedicineTakingListResponse.builder().request(request).medicineTakingList(medicineTakingList).build());
    }

    private List<MedicineTaking> putDone(@NonNull List<MedicineTaking> medicineTakingList, @NonNull Child child) {
        return Observable.fromIterable(medicineTakingList)
                .map(exercise -> putDone(exercise, child))
                .toList()
                .blockingGet();
    }

    private MedicineTaking putDone(@NonNull MedicineTaking medicineTaking, @NonNull Child child) {
        if (medicineTaking.getFinishDateTime() != null) {
            return medicineTaking.toBuilder().isDone(true).build();
        }
        val select = blockingEntityStore.count(MedicineTakingEventEntity.class)
                .join(MasterEventEntity.class).on(MasterEventEntity.ID.eq(MedicineTakingEventEntity.MASTER_EVENT_ID))
                .where(MedicineTakingEventEntity.MEDICINE_TAKING_ID.eq(medicineTaking.getId()))
                .and(MasterEventEntity.CHILD_ID.eq(child.getId()));
        int count;
        count = select.get()
                .value();
        boolean isEmpty = count == 0;
        count = select.and(MasterEventEntity.DONE.isNull().or(MasterEventEntity.DONE.eq(false)))
                .get()
                .value();
        boolean allDone = count == 0;
        return medicineTaking.toBuilder().isDone(!isEmpty && allDone).build();
    }

    private List<MedicineTaking> sort(List<MedicineTaking> medicineTakingList) {
        Collections.sort(medicineTakingList, (o1, o2) -> o1.isDone() == o2.isDone() ? 0 : (o1.isDone() ? 1 : -1));
        return medicineTakingList;
    }

    public Single<Boolean> hasConnectedEvents(@NonNull MedicineTaking medicineTaking) {
        return dataStore.count(MedicineTakingEventEntity.class)
                .where(MedicineTakingEventEntity.MEDICINE_TAKING_ID.eq(medicineTaking.getId()))
                .get()
                .single()
                .map(count -> count > 0);
    }

    public Single<Boolean> hasDataToFilter(@NonNull Child child) {
        return dataStore.count(MedicineTakingEntity.class)
                .where(MedicineTakingEntity.CHILD_ID.eq(child.getId()))
                .and(MedicineTakingEntity.DELETED.isNull().or(MedicineTakingEntity.DELETED.eq(false)))
                .get()
                .single()
                .map(count -> count > 0);
    }

    private RepeatParameters insertRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.insert(blockingEntityStore, repeatParameters, repeatParametersMapper);
    }

    private MedicineTaking insertMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return DbUtils.insert(blockingEntityStore, medicineTaking, medicineTakingMapper);
    }

    private RepeatParameters updateRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.update(blockingEntityStore, repeatParameters, repeatParametersMapper);
    }

    private MedicineTaking updateMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return DbUtils.update(blockingEntityStore, medicineTaking, medicineTakingMapper);
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

    public Observable<UpsertMedicineTakingResponse> add(@NonNull UpsertMedicineTakingRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            MedicineTaking medicineTaking = request.getMedicineTaking();
            RepeatParameters repeatParameters = upsertRepeatParameters(medicineTaking.getRepeatParameters());
            MedicineTaking result = medicineTaking.toBuilder().repeatParameters(repeatParameters).build();
            result = insertMedicineTaking(result);

            boolean needToAddEvents = ObjectUtils.isTrue(result.getIsExported());

            int count = 0;
            if (needToAddEvents) {
                count = eventsGenerator.generateEvents(result);
            }

            return UpsertMedicineTakingResponse.builder()
                    .request(request)
                    .addedEventsCount(count)
                    .medicineTaking(result)
                    .imageFilesToDelete(Collections.emptyList())
                    .build();
        }));
    }

    public Observable<UpsertMedicineTakingResponse> update(@NonNull UpsertMedicineTakingRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            MedicineTaking medicineTaking = request.getMedicineTaking();
            MedicineTakingEntity oldMedicineTakingEntity = blockingEntityStore.findByKey(MedicineTakingEntity.class, medicineTaking.getId());

            List<String> imageFilesToDelete = new ArrayList<>();
            if (!TextUtils.isEmpty(oldMedicineTakingEntity.getImageFileName())
                    && !oldMedicineTakingEntity.getImageFileName().equals(medicineTaking.getImageFileName())) {
                imageFilesToDelete.add(oldMedicineTakingEntity.getImageFileName());
            }

            boolean needToAddEvents = ObjectUtils.isFalse(oldMedicineTakingEntity.isExported())
                    && ObjectUtils.isTrue(medicineTaking.getIsExported());

            RepeatParameters repeatParameters = upsertRepeatParameters(medicineTaking.getRepeatParameters());
            MedicineTaking result = medicineTaking.toBuilder().repeatParameters(repeatParameters).build();
            result = updateMedicineTaking(result);

            int count = 0;
            if (needToAddEvents) {
                count = eventsGenerator.generateEvents(result);
            }

            return UpsertMedicineTakingResponse.builder()
                    .request(request)
                    .addedEventsCount(count)
                    .medicineTaking(result)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }

    public Observable<Integer> continueLinearGroup(@NonNull MedicineTaking medicineTaking,
                                                   @NonNull LocalDate sinceDate,
                                                   @NonNull Integer linearGroup,
                                                   @NonNull LengthValue lengthValue) {
        return Observable.fromCallable(
                () -> eventsGenerator.generateEvents(medicineTaking, sinceDate, linearGroup, lengthValue)
        );
    }
}
