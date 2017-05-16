package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.requests.CompleteDoctorVisitRequest;
import ru.android.childdiary.domain.interactors.medical.requests.CompleteDoctorVisitResponse;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitEventsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitEventsResponse;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitResponse;
import ru.android.childdiary.domain.interactors.medical.requests.GetDoctorVisitsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.GetDoctorVisitsResponse;

public interface DoctorVisitRepository {
    Observable<Doctor> getLastDoctor();

    Doctor setLastDoctor(@Nullable Doctor doctor);

    Observable<List<Doctor>> getDoctors();

    Observable<Doctor> addDoctor(@NonNull Doctor doctor);

    Observable<Doctor> deleteDoctor(@NonNull Doctor doctor);

    Observable<GetDoctorVisitsResponse> getDoctorVisits(@NonNull GetDoctorVisitsRequest request);

    Observable<DoctorVisit> addDoctorVisit(@NonNull DoctorVisit doctorVisit);

    Observable<DoctorVisit> updateDoctorVisit(@NonNull DoctorVisit doctorVisit);

    Observable<DeleteDoctorVisitResponse> deleteDoctorVisit(@NonNull DeleteDoctorVisitRequest request);

    Observable<DeleteDoctorVisitEventsResponse> deleteDoctorVisitWithEvents(@NonNull DeleteDoctorVisitEventsRequest request);

    Observable<CompleteDoctorVisitResponse> completeDoctorVisit(@NonNull CompleteDoctorVisitRequest request);
}
