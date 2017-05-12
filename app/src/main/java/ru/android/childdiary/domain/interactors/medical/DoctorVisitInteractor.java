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
import ru.android.childdiary.domain.interactors.child.ChildRepository;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.domain.interactors.medical.requests.DoctorVisitsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DoctorVisitsResponse;

public class DoctorVisitInteractor implements Interactor {
    private final ChildRepository childRepository;
    private final CalendarRepository calendarRepository;
    private final DoctorVisitRepository doctorVisitRepository;

    @Inject
    public DoctorVisitInteractor(ChildDataRepository childRepository,
                                 CalendarDataRepository calendarRepository,
                                 DoctorVisitDataRepository doctorVisitRepository) {
        this.childRepository = childRepository;
        this.calendarRepository = calendarRepository;
        this.doctorVisitRepository = doctorVisitRepository;
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

    public Observable<DoctorVisit> addDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return doctorVisitRepository.addDoctorVisit(doctorVisit);
    }

    public Observable<DoctorVisit> updateDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return doctorVisitRepository.updateDoctorVisit(doctorVisit);
    }

    public Observable<DoctorVisit> deleteDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return doctorVisitRepository.deleteDoctorVisit(doctorVisit);
    }
}
