package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.core.Repository;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;

public interface DoctorVisitRepository extends Repository {
    Observable<List<Doctor>> getDoctors();

    Observable<Doctor> addDoctor(@NonNull Doctor doctor);

    Observable<List<DoctorVisit>> getDoctorVisits();

    Observable<DoctorVisit> addDoctorVisit(@NonNull DoctorVisit doctorVisit);
}
