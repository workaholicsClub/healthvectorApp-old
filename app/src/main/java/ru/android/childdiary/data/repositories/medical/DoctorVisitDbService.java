package ru.android.childdiary.data.repositories.medical;

import android.support.annotation.NonNull;

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
import ru.android.childdiary.data.repositories.medical.mappers.DoctorMapper;
import ru.android.childdiary.data.repositories.medical.mappers.DoctorVisitMapper;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.requests.DoctorVisitsRequest;

@Singleton
public class DoctorVisitDbService {
    private final ReactiveEntityStore<Persistable> dataStore;

    @Inject
    public DoctorVisitDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    public Observable<List<Doctor>> getDoctors() {
        return dataStore.select(DoctorEntity.class)
                .orderBy(DoctorEntity.NAME, DoctorEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, DoctorMapper::mapToPlainObject));
    }

    public Observable<Doctor> addDoctor(@NonNull Doctor doctor) {
        return DbUtils.insertObservable(dataStore, doctor, DoctorMapper::mapToEntity, DoctorMapper::mapToPlainObject);
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
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, DoctorVisitMapper::mapToPlainObject));
    }

    private RepeatParameters insertRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.insert(dataStore, repeatParameters,
                RepeatParametersMapper::mapToEntity, RepeatParametersMapper::mapToPlainObject);
    }

    private DoctorVisit insertDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return DbUtils.insert(dataStore, doctorVisit,
                DoctorVisitMapper::mapToEntity, DoctorVisitMapper::mapToPlainObject);
    }

    public Observable<DoctorVisit> add(@NonNull DoctorVisit doctorVisit) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            DoctorVisit object = doctorVisit;
            RepeatParameters repeatParameters = object.getRepeatParameters();
            if (repeatParameters != null) {
                repeatParameters = insertRepeatParameters(repeatParameters);
                object = object.toBuilder().repeatParameters(repeatParameters).build();
            }
            return insertDoctorVisit(object);
        }));
    }

    private RepeatParameters updateRepeatParameters(@NonNull RepeatParameters repeatParameters) {
        return DbUtils.update(dataStore, repeatParameters,
                RepeatParametersMapper::mapToEntity, RepeatParametersMapper::mapToPlainObject);
    }

    private DoctorVisit updateDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return DbUtils.update(dataStore, doctorVisit,
                DoctorVisitMapper::mapToEntity, DoctorVisitMapper::mapToPlainObject);
    }

    public Observable<DoctorVisit> update(@NonNull DoctorVisit doctorVisit) {
        return Observable.fromCallable(() -> dataStore.toBlocking().runInTransaction(() -> {
            DoctorVisit object = doctorVisit;
            RepeatParameters repeatParameters = object.getRepeatParameters();
            if (repeatParameters != null) {
                if (repeatParameters.getId() == null) {
                    repeatParameters = insertRepeatParameters(repeatParameters);
                } else {
                    repeatParameters = updateRepeatParameters(repeatParameters);
                }
                object = object.toBuilder().repeatParameters(repeatParameters).build();
            }
            return updateDoctorVisit(object);
        }));
    }
}
