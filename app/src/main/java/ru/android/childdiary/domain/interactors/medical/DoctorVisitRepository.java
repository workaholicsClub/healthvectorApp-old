package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.requests.DoctorVisitsRequest;

public interface DoctorVisitRepository {
    Observable<Doctor> getLastDoctor();

    Doctor setLastDoctor(@Nullable Doctor doctor);

    Observable<List<Doctor>> getDoctors();

    Observable<Doctor> addDoctor(@NonNull Doctor doctor);

    Observable<Doctor> deleteDoctor(@NonNull Doctor doctor);

    Observable<List<DoctorVisit>> getDoctorVisits(@NonNull DoctorVisitsRequest request);

    Observable<DoctorVisit> addDoctorVisit(@NonNull DoctorVisit doctorVisit);

    Observable<DoctorVisit> updateDoctorVisit(@NonNull DoctorVisit doctorVisit);

    Observable<DoctorVisit> deleteDoctorVisit(@NonNull DoctorVisit doctorVisit);
}
