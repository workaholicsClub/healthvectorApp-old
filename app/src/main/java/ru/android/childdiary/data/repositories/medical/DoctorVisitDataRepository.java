package ru.android.childdiary.data.repositories.medical;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.DoctorVisitRepository;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.requests.DoctorVisitsRequest;

@Singleton
public class DoctorVisitDataRepository implements DoctorVisitRepository {
    private final DoctorVisitDbService dbService;

    @Inject
    public DoctorVisitDataRepository(DoctorVisitDbService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Observable<List<Doctor>> getDoctors() {
        return dbService.getDoctors();
    }

    @Override
    public Observable<Doctor> addDoctor(@NonNull Doctor doctor) {
        return dbService.addDoctor(doctor);
    }

    @Override
    public Observable<List<DoctorVisit>> getDoctorVisits(@NonNull DoctorVisitsRequest request) {
        return dbService.getDoctorVisits(request);
    }

    @Override
    public Observable<DoctorVisit> addDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return dbService.add(doctorVisit);
    }

    @Override
    public Observable<DoctorVisit> updateDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return dbService.update(doctorVisit);
    }
}
