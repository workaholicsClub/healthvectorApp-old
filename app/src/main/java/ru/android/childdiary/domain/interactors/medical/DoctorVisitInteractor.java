package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.calendar.CalendarDataRepository;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.data.repositories.medical.DoctorVisitDataRepository;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.core.Interactor;
import ru.android.childdiary.domain.interactors.calendar.CalendarRepository;
import ru.android.childdiary.domain.interactors.calendar.validation.CalendarValidationResult;
import ru.android.childdiary.domain.interactors.child.ChildRepository;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.requests.DoctorVisitsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DoctorVisitsResponse;
import ru.android.childdiary.domain.interactors.medical.validation.DoctorVisitValidator;
import ru.android.childdiary.domain.interactors.medical.validation.MedicalValidationException;
import ru.android.childdiary.domain.interactors.medical.validation.MedicalValidationResult;

public class DoctorVisitInteractor implements Interactor {
    private final ChildRepository childRepository;
    private final CalendarRepository calendarRepository;
    private final DoctorVisitRepository doctorVisitRepository;
    private final DoctorVisitValidator doctorVisitValidator;

    @Inject
    public DoctorVisitInteractor(ChildDataRepository childRepository,
                                 CalendarDataRepository calendarRepository,
                                 DoctorVisitDataRepository doctorVisitRepository,
                                 DoctorVisitValidator doctorVisitValidator) {
        this.childRepository = childRepository;
        this.calendarRepository = calendarRepository;
        this.doctorVisitRepository = doctorVisitRepository;
        this.doctorVisitValidator = doctorVisitValidator;
    }

    public Observable<List<Doctor>> getDoctors() {
        return doctorVisitRepository.getDoctors();
    }

    public Observable<Doctor> addDoctor(@NonNull Doctor doctor) {
        return doctorVisitRepository.addDoctor(doctor);
    }

    public Observable<Doctor> deleteDoctor(@NonNull Doctor doctor) {
        return doctorVisitRepository.deleteDoctor(doctor);
    }

    public Observable<DoctorVisit> getDefaultDoctorVisit() {
        return Observable.combineLatest(
                childRepository.getActiveChildOnce(),
                getDefaultRepeatParameters(),
                Observable.just(DateTime.now()),
                calendarRepository.getDefaultNotifyTimeInMinutes(EventType.DOCTOR_VISIT),
                (child, repeatParameters, dateTime, minutes) -> DoctorVisit.builder()
                        .child(child)
                        .doctor(null)
                        .repeatParameters(repeatParameters)
                        .name(null)
                        .durationInMinutes(15)
                        .dateTime(dateTime)
                        .finishDateTime(null)
                        .exported(true)
                        .notifyTimeInMinutes(minutes)
                        .note(null)
                        .imageFileName(null)
                        .build());
    }

    private Observable<RepeatParameters> getDefaultRepeatParameters() {
        return Observable.just(
                RepeatParameters.builder()
                        .frequency(LinearGroups.builder()
                                .times(new ArrayList<>(Collections.emptyList()))
                                .build())
                        .periodicity(null)
                        .length(null)
                        .build());
    }

    public Observable<DoctorVisitsResponse> getDoctorVisits(@NonNull DoctorVisitsRequest request) {
        return doctorVisitRepository.getDoctorVisits(request)
                .map(doctorVisits -> DoctorVisitsResponse.builder().request(request).doctorVisits(doctorVisits).build());
    }

    private Observable<DoctorVisit> validate(@NonNull DoctorVisit doctorVisit) {
        return Observable.just(doctorVisit)
                .flatMap(item -> {
                    List<MedicalValidationResult> results = doctorVisitValidator.validate(item);
                    if (!doctorVisitValidator.isValid(results)) {
                        return Observable.error(new MedicalValidationException(results));
                    }
                    return Observable.just(item);
                });
    }

    public Observable<DoctorVisit> addDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return validate(doctorVisit).flatMap(doctorVisitRepository::addDoctorVisit);
    }

    public Observable<DoctorVisit> updateDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return validate(doctorVisit).flatMap(doctorVisitRepository::updateDoctorVisit);
    }

    public Observable<DoctorVisit> deleteDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return doctorVisitRepository.deleteDoctorVisit(doctorVisit);
    }
}
