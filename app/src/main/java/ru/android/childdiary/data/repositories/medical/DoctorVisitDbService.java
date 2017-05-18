package ru.android.childdiary.data.repositories.medical;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.requery.BlockingEntityStore;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.entities.calendar.events.DoctorVisitEventEntity;
import ru.android.childdiary.data.entities.medical.DoctorVisitEntity;
import ru.android.childdiary.data.entities.medical.core.DoctorEntity;
import ru.android.childdiary.data.repositories.calendar.mappers.RepeatParametersMapper;
import ru.android.childdiary.data.repositories.core.DoctorVisitEventsGenerator;
import ru.android.childdiary.data.repositories.medical.mappers.DoctorMapper;
import ru.android.childdiary.data.repositories.medical.mappers.DoctorVisitMapper;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.requests.GetDoctorVisitsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.GetDoctorVisitsResponse;
import ru.android.childdiary.domain.interactors.medical.requests.UpsertDoctorVisitRequest;
import ru.android.childdiary.domain.interactors.medical.requests.UpsertDoctorVisitResponse;
import ru.android.childdiary.utils.ObjectUtils;

@Singleton
public class DoctorVisitDbService {
    private final Logger logger = LoggerFactory.getLogger(toString());
    private final ReactiveEntityStore<Persistable> dataStore;
    private final BlockingEntityStore<Persistable> blockingEntityStore;
    private final DoctorVisitEventsGenerator eventsGenerator;
    private final DoctorMapper doctorMapper;
    private final DoctorVisitMapper doctorVisitMapper;
    private final RepeatParametersMapper repeatParametersMapper;

    @Inject
    public DoctorVisitDbService(ReactiveEntityStore<Persistable> dataStore,
                                DoctorVisitEventsGenerator eventsGenerator,
                                DoctorMapper doctorMapper,
                                DoctorVisitMapper doctorVisitMapper,
                                RepeatParametersMapper repeatParametersMapper) {
        this.dataStore = dataStore;
        this.blockingEntityStore = dataStore.toBlocking();
        this.eventsGenerator = eventsGenerator;
        this.doctorMapper = doctorMapper;
        this.doctorVisitMapper = doctorVisitMapper;
        this.repeatParametersMapper = repeatParametersMapper;
    }

    public Observable<List<Doctor>> getDoctors() {
        return dataStore.select(DoctorEntity.class)
                .orderBy(DoctorEntity.NAME, DoctorEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, doctorMapper));
    }

    public Observable<Doctor> addDoctor(@NonNull Doctor doctor) {
        return DbUtils.insertObservable(blockingEntityStore, doctor, doctorMapper);
    }

    public Observable<Doctor> deleteDoctor(@NonNull Doctor doctor) {
        return DbUtils.deleteObservable(blockingEntityStore, DoctorEntity.class, doctor, doctor.getId());
    }

    public Observable<GetDoctorVisitsResponse> getDoctorVisits(@NonNull GetDoctorVisitsRequest request) {
        Child child = request.getChild();
        return dataStore.select(DoctorVisitEntity.class)
                .where(DoctorVisitEntity.CHILD_ID.eq(child.getId()))
                .and(DoctorVisitEntity.DELETED.isNull().or(DoctorVisitEntity.DELETED.eq(false)))
                .orderBy(DoctorVisitEntity.DATE_TIME, DoctorVisitEntity.DOCTOR_ID, DoctorVisitEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, doctorVisitMapper))
                .map(doctorVisits -> GetDoctorVisitsResponse.builder().request(request).doctorVisits(doctorVisits).build());
    }

    public Single<Boolean> hasConnectedEvents(@lombok.NonNull DoctorVisit doctorVisit) {
        return dataStore.count(DoctorVisitEventEntity.class)
                .where(DoctorVisitEventEntity.DOCTOR_VISIT_ID.eq(doctorVisit.getId()))
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
                    .build();
        }));
    }

    public Observable<UpsertDoctorVisitResponse> update(@NonNull UpsertDoctorVisitRequest request) {
        return Observable.fromCallable(() -> blockingEntityStore.runInTransaction(() -> {
            DoctorVisit doctorVisit = request.getDoctorVisit();
            DoctorVisitEntity oldDoctorVisitEntity = blockingEntityStore
                    .select(DoctorVisitEntity.class)
                    .where(DoctorVisitEntity.ID.eq(doctorVisit.getId()))
                    .get()
                    .first();

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
                    .build();
        }));
    }
}
