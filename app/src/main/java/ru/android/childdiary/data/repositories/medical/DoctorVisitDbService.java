package ru.android.childdiary.data.repositories.medical;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
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
import ru.android.childdiary.domain.interactors.medical.requests.DoctorVisitsRequest;
import ru.android.childdiary.utils.ObjectUtils;

@Singleton
public class DoctorVisitDbService {
    private final ReactiveEntityStore<Persistable> dataStore;
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
        return DbUtils.insertObservable(dataStore, doctor, doctorMapper);
    }

    public Observable<Doctor> deleteDoctor(@NonNull Doctor doctor) {
        return DbUtils.deleteObservable(dataStore, DoctorEntity.class, doctor, doctor.getId());
    }

    public Observable<List<DoctorVisit>> getDoctorVisits(@NonNull DoctorVisitsRequest request) {
        Child child = request.getChild();
        return dataStore.select(DoctorVisitEntity.class)
                .where(DoctorVisitEntity.CHILD_ID.eq(child.getId()))
                .orderBy(DoctorVisitEntity.DATE_TIME, DoctorVisitEntity.DOCTOR_ID, DoctorVisitEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, doctorVisitMapper));
    }

    private RepeatParameters insertRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.insert(dataStore, repeatParameters, repeatParametersMapper);
    }

    private DoctorVisit insertDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return DbUtils.insert(dataStore, doctorVisit, doctorVisitMapper);
    }

    private RepeatParameters updateRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.update(dataStore, repeatParameters, repeatParametersMapper);
    }

    private DoctorVisit updateDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return DbUtils.update(dataStore, doctorVisit, doctorVisitMapper);
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

    public Observable<DoctorVisit> add(@NonNull DoctorVisit doctorVisit) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            RepeatParameters repeatParameters = upsertRepeatParameters(doctorVisit.getRepeatParameters());
            DoctorVisit result = doctorVisit.toBuilder().repeatParameters(repeatParameters).build();
            result = insertDoctorVisit(result);

            eventsGenerator.generateEvents(result);

            return result;
        }));
    }

    public Observable<DoctorVisit> update(@NonNull DoctorVisit doctorVisit) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            DoctorVisitEntity oldDoctorVisitEntity = dataStore.toBlocking()
                    .select(DoctorVisitEntity.class)
                    .where(DoctorVisitEntity.ID.eq(doctorVisit.getId()))
                    .get()
                    .first();

            boolean needToAddEvents = ObjectUtils.isFalse(oldDoctorVisitEntity.getExported())
                    && ObjectUtils.isTrue(doctorVisit.getExported());

            RepeatParameters repeatParameters = upsertRepeatParameters(doctorVisit.getRepeatParameters());
            DoctorVisit result = doctorVisit.toBuilder().repeatParameters(repeatParameters).build();
            result = updateDoctorVisit(result);

            if (needToAddEvents) {
                eventsGenerator.generateEvents(result);
            }

            return result;
        }));
    }

    public Observable<DoctorVisit> delete(@NonNull DoctorVisit doctorVisit) {
        return DbUtils.deleteObservable(dataStore, DoctorVisitEntity.class, doctorVisit, doctorVisit.getId());
    }
}
