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
import ru.android.childdiary.R;
import ru.android.childdiary.domain.interactors.medical.DoctorVisit;
import ru.android.childdiary.domain.interactors.medical.DoctorVisitRepository;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.requests.DoctorVisitsRequest;
import ru.android.childdiary.utils.ObjectUtils;

@Singleton
public class DoctorVisitDataRepository implements DoctorVisitRepository {
    private static final String KEY_LAST_DOCTOR = "last_doctor";

    private final Context context;
    private final RxSharedPreferences preferences;
    private final DoctorVisitDbService dbService;

    @Inject
    public DoctorVisitDataRepository(Context context,
                                     RxSharedPreferences preferences,
                                     DoctorVisitDbService dbService) {
        this.context = context;
        this.preferences = preferences;
        this.dbService = dbService;
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
        return dbService.getDoctors();
    }

    @Override
    public Observable<Doctor> addDoctor(@NonNull Doctor doctor) {
        return dbService.addDoctor(doctor);
    }

    @Override
    public Observable<Doctor> deleteDoctor(@NonNull Doctor doctor) {
        return dbService.deleteDoctor(doctor);
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

    @Override
    public Observable<DoctorVisit> deleteDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return dbService.delete(doctorVisit);
    }
}
