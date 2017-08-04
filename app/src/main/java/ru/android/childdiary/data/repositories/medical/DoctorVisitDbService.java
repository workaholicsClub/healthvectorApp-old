package ru.android.childdiary.data.repositories.medical;

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
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.db.entities.calendar.DoctorVisitEventEntity;
import ru.android.childdiary.data.db.entities.calendar.core.MasterEventEntity;
import ru.android.childdiary.data.db.entities.medical.DoctorVisitEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.RepeatParametersMapper;
import ru.android.childdiary.data.repositories.core.generators.DoctorVisitEventsGenerator;
import ru.android.childdiary.data.repositories.medical.mappers.DoctorVisitMapper;
import ru.android.childdiary.domain.interactors.calendar.data.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.domain.interactors.medical.data.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.requests.GetDoctorVisitsFilter;
import ru.android.childdiary.domain.interactors.medical.requests.GetDoctorVisitsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.GetDoctorVisitsResponse;
import ru.android.childdiary.domain.interactors.medical.requests.UpsertDoctorVisitRequest;
import ru.android.childdiary.domain.interactors.medical.requests.UpsertDoctorVisitResponse;
import ru.android.childdiary.utils.ObjectUtils;
import ru.android.childdiary.utils.strings.DateUtils;

@Singleton
public class DoctorVisitDbService {
    private final Logger logger = LoggerFactory.getLogger(toString());
    private final ReactiveEntityStore<Persistable> dataStore;
    private final BlockingEntityStore<Persistable> blockingEntityStore;
    private final DoctorVisitEventsGenerator eventsGenerator;
    private final DoctorVisitMapper doctorVisitMapper;
    private final RepeatParametersMapper repeatParametersMapper;

    @Inject
    public DoctorVisitDbService(ReactiveEntityStore<Persistable> dataStore,
                                DoctorVisitEventsGenerator eventsGenerator,
                                DoctorVisitMapper doctorVisitMapper,
                                RepeatParametersMapper repeatParametersMapper) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
        this.eventsGenerator = eventsGenerator;
        this.doctorVisitMapper = doctorVisitMapper;
        this.repeatParametersMapper = repeatParametersMapper;
    }

    public Observable<GetDoctorVisitsResponse> getDoctorVisits(@NonNull GetDoctorVisitsRequest request) {
        Child child = request.getChild();
        WhereAndOr<ReactiveResult<DoctorVisitEntity>> select = dataStore.select(DoctorVisitEntity.class)
                .where(DoctorVisitEntity.CHILD_ID.eq(child.getId()))
                .and(DoctorVisitEntity.DELETED.isNull().or(DoctorVisitEntity.DELETED.eq(false)));

        GetDoctorVisitsFilter filter = request.getFilter();

        LocalDate fromDate = filter.getFromDate();
        if (fromDate != null) {
            select = select.and(DoctorVisitEntity.DATE_TIME.greaterThanOrEqual(DateUtils.midnight(fromDate)));
        }

        LocalDate toDate = filter.getToDate();
        if (toDate != null) {
            select = select.and(DoctorVisitEntity.DATE_TIME.lessThan(DateUtils.nextDayMidnight(toDate)));
        }

        List<Doctor> doctors = filter.getSelectedItems();
        if (!doctors.isEmpty()) {
            List<Long> ids = Observable.fromIterable(doctors)
                    .filter(doctor -> doctor.getId() != null)
                    .map(Doctor::getId)
                    .toList()
                    .blockingGet();
            select = select.and(DoctorVisitEntity.DOCTOR_ID.in(ids));
        }

        return select.orderBy(DoctorVisitEntity.DATE_TIME.desc(), DoctorVisitEntity.DOCTOR_ID, DoctorVisitEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, doctorVisitMapper))
                .map(doctorVisits -> putDone(doctorVisits, child))
                .map(this::sort)
                .map(doctorVisits -> GetDoctorVisitsResponse.builder().request(request).doctorVisits(doctorVisits).build());
    }

    private List<DoctorVisit> putDone(@NonNull List<DoctorVisit> doctorVisits, @NonNull Child child) {
        return Observable.fromIterable(doctorVisits)
                .map(doctorVisit -> putDone(doctorVisit, child))
                .toList()
                .blockingGet();
    }

    private DoctorVisit putDone(@NonNull DoctorVisit doctorVisit, @NonNull Child child) {
        if (doctorVisit.getFinishDateTime() != null) {
            return doctorVisit.toBuilder().isDone(true).build();
        }
        List<DoctorVisitEventEntity> events = blockingEntityStore.select(DoctorVisitEventEntity.class)
                .join(MasterEventEntity.class).on(MasterEventEntity.ID.eq(DoctorVisitEventEntity.MASTER_EVENT_ID))
                .where(DoctorVisitEventEntity.DOCTOR_VISIT_ID.eq(doctorVisit.getId()))
                .and(MasterEventEntity.CHILD_ID.eq(child.getId()))
                .get()
                .toList();
        boolean isEmpty = events.isEmpty();
        long count = Observable.fromIterable(events)
                .filter(event -> !ObjectUtils.isTrue(event.getMasterEvent().isDone()))
                .count()
                .blockingGet();
        boolean allDone = count == 0;
        return doctorVisit.toBuilder().isDone(!isEmpty && allDone).build();
    }

    private List<DoctorVisit> sort(List<DoctorVisit> doctorVisits) {
        Collections.sort(doctorVisits, (o1, o2) -> o1.isDone() == o2.isDone() ? 0 : (o1.isDone() ? 1 : -1));
        return doctorVisits;
    }

    public Single<Boolean> hasConnectedEvents(@NonNull DoctorVisit doctorVisit) {
        return dataStore.count(DoctorVisitEventEntity.class)
                .where(DoctorVisitEventEntity.DOCTOR_VISIT_ID.eq(doctorVisit.getId()))
                .get()
                .single()
                .map(count -> count > 0);
    }

    public Single<Boolean> hasDataToFilter(@NonNull Child child) {
        return dataStore.count(DoctorVisitEntity.class)
                .where(DoctorVisitEntity.CHILD_ID.eq(child.getId()))
                .and(DoctorVisitEntity.DELETED.isNull().or(DoctorVisitEntity.DELETED.eq(false)))
                .get()
                .single()
                .map(count -> count > 0);
    }

    private RepeatParameters insertRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.insert(blockingEntityStore, repeatParameters, repeatParametersMapper);
    }

    private DoctorVisit insertDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return DbUtils.insert(blockingEntityStore, doctorVisit, doctorVisitMapper);
    }

    private RepeatParameters updateRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.update(blockingEntityStore, repeatParameters, repeatParametersMapper);
    }

    private DoctorVisit updateDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return DbUtils.update(blockingEntityStore, doctorVisit, doctorVisitMapper);
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

    public Observable<UpsertDoctorVisitResponse> add(@NonNull UpsertDoctorVisitRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            DoctorVisit doctorVisit = request.getDoctorVisit();
            RepeatParameters repeatParameters = upsertRepeatParameters(doctorVisit.getRepeatParameters());
            DoctorVisit result = doctorVisit.toBuilder().repeatParameters(repeatParameters).build();
            result = insertDoctorVisit(result);

            boolean needToAddEvents = ObjectUtils.isTrue(result.getIsExported());

            int count = 0;
            if (needToAddEvents) {
                count = eventsGenerator.generateEvents(result);
            }

            return UpsertDoctorVisitResponse.builder()
                    .request(request)
                    .addedEventsCount(count)
                    .doctorVisit(result)
                    .imageFilesToDelete(Collections.emptyList())
                    .build();
        }));
    }

    public Observable<UpsertDoctorVisitResponse> update(@NonNull UpsertDoctorVisitRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            DoctorVisit doctorVisit = request.getDoctorVisit();
            DoctorVisitEntity oldDoctorVisitEntity = blockingEntityStore.findByKey(DoctorVisitEntity.class, doctorVisit.getId());

            List<String> imageFilesToDelete = new ArrayList<>();
            if (!TextUtils.isEmpty(oldDoctorVisitEntity.getImageFileName())
                    && !oldDoctorVisitEntity.getImageFileName().equals(doctorVisit.getImageFileName())) {
                imageFilesToDelete.add(oldDoctorVisitEntity.getImageFileName());
            }

            boolean needToAddEvents = ObjectUtils.isFalse(oldDoctorVisitEntity.isExported())
                    && ObjectUtils.isTrue(doctorVisit.getIsExported());

            RepeatParameters repeatParameters = upsertRepeatParameters(doctorVisit.getRepeatParameters());
            DoctorVisit result = doctorVisit.toBuilder().repeatParameters(repeatParameters).build();
            result = updateDoctorVisit(result);

            int count = 0;
            if (needToAddEvents) {
                count = eventsGenerator.generateEvents(result);
            }

            return UpsertDoctorVisitResponse.builder()
                    .request(request)
                    .addedEventsCount(count)
                    .doctorVisit(result)
                    .imageFilesToDelete(imageFilesToDelete)
                    .build();
        }));
    }
}
