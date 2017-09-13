package ru.android.childdiary.data.repositories.dictionaries.doctors;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.data.db.DbUtils;
import ru.android.childdiary.data.db.entities.dictionaries.DoctorEntity;
import ru.android.childdiary.data.repositories.dictionaries.core.BaseCrudDbService;
import ru.android.childdiary.data.repositories.dictionaries.doctors.mappers.DoctorMapper;
import ru.android.childdiary.domain.dictionaries.doctors.data.Doctor;

@Singleton
public class DoctorDbService extends BaseCrudDbService<Doctor> {
    private final DoctorMapper doctorMapper;

    @Inject
    public DoctorDbService(ReactiveEntityStore<Persistable> dataStore,
                           DoctorMapper doctorMapper) {
        super(dataStore);
        this.doctorMapper = doctorMapper;
    }

    @Override
    public Observable<List<Doctor>> getAll() {
        return dataStore.select(DoctorEntity.class)
                .orderBy(DoctorEntity.ID)
                .get()
                .observableResult()
                .flatMap(reactiveResult -> DbUtils.mapReactiveResultToListObservable(reactiveResult, doctorMapper));
    }

    @Override
    public Observable<Doctor> add(@NonNull Doctor doctor) {
        return DbUtils.insertObservable(blockingEntityStore, doctor, doctorMapper);
    }

    @Override
    public Observable<Doctor> update(@NonNull Doctor doctor) {
        return DbUtils.updateObservable(blockingEntityStore, doctor, doctorMapper);
    }

    @Override
    public Observable<Doctor> delete(@NonNull Doctor doctor) {
        return DbUtils.deleteObservable(blockingEntityStore, DoctorEntity.class, doctor, doctor.getId());
    }
}
