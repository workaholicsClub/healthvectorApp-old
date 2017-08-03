package ru.android.childdiary.data.repositories.medical;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.R;
import ru.android.childdiary.data.repositories.calendar.CleanUpDbService;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.medical.data.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.DoctorVisitRepository;
import ru.android.childdiary.domain.interactors.dictionaries.doctors.Doctor;
import ru.android.childdiary.domain.interactors.medical.requests.CompleteDoctorVisitRequest;
import ru.android.childdiary.domain.interactors.medical.requests.CompleteDoctorVisitResponse;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitEventsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitEventsResponse;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteDoctorVisitResponse;
import ru.android.childdiary.domain.interactors.medical.requests.GetDoctorVisitsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.GetDoctorVisitsResponse;
import ru.android.childdiary.domain.interactors.medical.requests.UpsertDoctorVisitRequest;
import ru.android.childdiary.domain.interactors.medical.requests.UpsertDoctorVisitResponse;
import ru.android.childdiary.utils.ObjectUtils;

@Singleton
public class DoctorVisitDataRepository implements DoctorVisitRepository {
    private static final String KEY_LAST_DOCTOR = "last_doctor";

    private final Context context;
    private final RxSharedPreferences preferences;
    private final DoctorVisitDbService doctorVisitDbService;
    private final CleanUpDbService cleanUpDbService;

    @Inject
    public DoctorVisitDataRepository(Context context,
                                     RxSharedPreferences preferences,
                                     DoctorVisitDbService doctorVisitDbService,
                                     CleanUpDbService cleanUpDbService) {
        this.context = context;
        this.preferences = preferences;
        this.doctorVisitDbService = doctorVisitDbService;
        this.cleanUpDbService = cleanUpDbService;
    }

    @Override
    public Observable<Doctor> getLastDoctor() {
        return Observable.combineLatest(
                getDoctors()
                        .first(Collections.singletonList(Doctor.NULL))
                        .toObservable(),
                preferences
                        .getLong(KEY_LAST_DOCTOR)
                        .asObservable()
                        .first(0L)
                        .toObservable(),
                (doctors, id) -> {
                    Doctor doctor = getLastDoctor(doctors, id);
                    if (doctor == Doctor.NULL) {
                        doctor = getDefaultDoctor(doctors);
                    }
                    return doctor;
                });
    }

    private Doctor getLastDoctor(@NonNull List<Doctor> doctors, Long id) {
        return Observable
                .fromIterable(doctors)
                .filter(food -> ObjectUtils.equals(food.getId(), id))
                .first(Doctor.NULL)
                .blockingGet();
    }

    private Doctor getDefaultDoctor(@NonNull List<Doctor> doctors) {
        String defaultDoctorName = context.getString(R.string.default_doctor_name);
        return Observable
                .fromIterable(doctors)
                .filter(doctor -> ObjectUtils.equals(defaultDoctorName, doctor.getName()))
                .first(Doctor.NULL)
                .blockingGet();
    }

    @Override
    public Doctor setLastDoctor(@Nullable Doctor doctor) {
        preferences.getLong(KEY_LAST_DOCTOR).set(doctor == null ? null : doctor.getId());
        return doctor;
    }

    @Override
    public Observable<List<Doctor>> getDoctors() {
        return doctorVisitDbService.getDoctors();
    }

    @Override
    public Observable<Doctor> addDoctor(@NonNull Doctor doctor) {
        return doctorVisitDbService.addDoctor(doctor);
    }

    @Override
    public Observable<Doctor> deleteDoctor(@NonNull Doctor doctor) {
        return doctorVisitDbService.deleteDoctor(doctor);
    }

    @Override
    public Observable<GetDoctorVisitsResponse> getDoctorVisits(@NonNull GetDoctorVisitsRequest request) {
        return doctorVisitDbService.getDoctorVisits(request);
    }

    @Override
    public Single<Boolean> hasConnectedEvents(@NonNull DoctorVisit doctorVisit) {
        return doctorVisitDbService.hasConnectedEvents(doctorVisit);
    }

    @Override
    public Single<Boolean> hasDataToFilter(@NonNull Child child) {
        return doctorVisitDbService.hasDataToFilter(child);
    }

    @Override
    public Observable<UpsertDoctorVisitResponse> addDoctorVisit(@NonNull UpsertDoctorVisitRequest request) {
        return doctorVisitDbService.add(request);
    }

    @Override
    public Observable<UpsertDoctorVisitResponse> updateDoctorVisit(@NonNull UpsertDoctorVisitRequest request) {
        return doctorVisitDbService.update(request);
    }

    @Override
    public Observable<DeleteDoctorVisitResponse> deleteDoctorVisit(@NonNull DeleteDoctorVisitRequest request) {
        return cleanUpDbService.deleteDoctorVisit(request);
    }

    @Override
    public Observable<DeleteDoctorVisitEventsResponse> deleteDoctorVisitWithEvents(@NonNull DeleteDoctorVisitEventsRequest request) {
        return cleanUpDbService.deleteDoctorVisitEvents(request);
    }

    @Override
    public Observable<CompleteDoctorVisitResponse> completeDoctorVisit(@NonNull CompleteDoctorVisitRequest request) {
        return cleanUpDbService.completeDoctorVisit(request);
    }
}
