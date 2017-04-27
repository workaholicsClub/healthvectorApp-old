package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.medical.DoctorVisitDataRepository;
import ru.android.childdiary.domain.core.Interactor;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

public class DoctorVisitInteractor implements Interactor {
    private final DoctorVisitRepository doctorVisitRepository;

    @Inject
    public DoctorVisitInteractor(DoctorVisitDataRepository doctorVisitRepository) {
        this.doctorVisitRepository = doctorVisitRepository;
    }

    public Observable<List<Doctor>> getDoctors() {
        return doctorVisitRepository.getDoctors();
    }

    public Observable<Doctor> addDoctor(@NonNull Doctor doctor) {
        return doctorVisitRepository.addDoctor(doctor);
    }

    public Observable<List<DoctorVisit>> getDoctorVisits() {
        return doctorVisitRepository.getDoctorVisits();
    }

    public Observable<DoctorVisit> addDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return doctorVisitRepository.addDoctorVisit(doctorVisit);
    }
}
