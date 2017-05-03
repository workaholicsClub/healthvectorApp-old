package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.core.Repository;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.requests.DoctorVisitsRequest;

public interface DoctorVisitRepository extends Repository {
    Observable<List<Doctor>> getDoctors();

    Observable<Doctor> addDoctor(@NonNull Doctor doctor);

    Observable<List<DoctorVisit>> getDoctorVisits(@NonNull DoctorVisitsRequest request);

    Observable<DoctorVisit> addDoctorVisit(@NonNull DoctorVisit doctorVisit);
}
