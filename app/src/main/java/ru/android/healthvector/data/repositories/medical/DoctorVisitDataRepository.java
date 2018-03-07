package ru.android.healthvector.data.repositories.medical;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.healthvector.R;
import ru.android.healthvector.data.repositories.calendar.CleanUpDbService;
import ru.android.healthvector.data.repositories.dictionaries.core.CrudDbService;
import ru.android.healthvector.data.repositories.dictionaries.doctors.DoctorDbService;
import ru.android.healthvector.domain.calendar.data.core.LengthValue;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;
import ru.android.healthvector.domain.medical.DoctorVisitRepository;
import ru.android.healthvector.domain.medical.data.DoctorVisit;
import ru.android.healthvector.domain.medical.requests.CompleteDoctorVisitRequest;
import ru.android.healthvector.domain.medical.requests.CompleteDoctorVisitResponse;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitEventsRequest;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitEventsResponse;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitRequest;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitResponse;
import ru.android.healthvector.domain.medical.requests.GetDoctorVisitsRequest;
import ru.android.healthvector.domain.medical.requests.GetDoctorVisitsResponse;
import ru.android.healthvector.domain.medical.requests.UpsertDoctorVisitRequest;
import ru.android.healthvector.domain.medical.requests.UpsertDoctorVisitResponse;
import ru.android.healthvector.utils.ObjectUtils;

@Singleton
public class DoctorVisitDataRepository implements DoctorVisitRepository {
    private static final String KEY_LAST_DOCTOR = "last_doctor";

    private final Context context;
    private final RxSharedPreferences preferences;
    private final DoctorVisitDbService doctorVisitDbService;
    private final CleanUpDbService cleanUpDbService;
    private final CrudDbService<Doctor> doctorDbService;

    @Inject
    public DoctorVisitDataRepository(Context context,
                                     RxSharedPreferences preferences,
                                     DoctorVisitDbService doctorVisitDbService,
                                     CleanUpDbService cleanUpDbService,
                                     DoctorDbService doctorDbService) {
        this.context = context;
        this.preferences = preferences;
        this.doctorVisitDbService = doctorVisitDbService;
        this.cleanUpDbService = cleanUpDbService;
        this.doctorDbService = doctorDbService;
    }

    @Override
    public Observable<Doctor> getLastDoctor() {
        return Observable.combineLatest(
                doctorDbService.getAll()
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

    @Override
    public Observable<Integer> continueLinearGroup(@NonNull DoctorVisit doctorVisit,
                                                   @NonNull LocalDate sinceDate,
                                                   @NonNull Integer linearGroup,
                                                   @NonNull LengthValue lengthValue) {
        return doctorVisitDbService.continueLinearGroup(doctorVisit, sinceDate, linearGroup, lengthValue);
    }
}
