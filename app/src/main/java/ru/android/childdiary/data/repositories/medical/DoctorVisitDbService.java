package ru.android.childdiary.data.repositories.medical;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

@Singleton
public class DoctorVisitDbService {
    private final ReactiveEntityStore<Persistable> dataStore;

    @Inject
    public DoctorVisitDbService(ReactiveEntityStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    public Observable<List<Doctor>> getDoctors() {
        return null;
    }

    public Observable<Doctor> addDoctor(@NonNull Doctor doctor) {
        return null;
    }

    public Observable<List<DoctorVisit>> getDoctorVisits() {
        return null;
    }

    public Observable<DoctorVisit> addDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return null;
    }
}
