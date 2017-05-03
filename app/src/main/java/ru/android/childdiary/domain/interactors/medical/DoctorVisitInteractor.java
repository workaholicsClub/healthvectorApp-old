package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

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

    public Observable<DoctorVisit> getDefaultOtherEvent() {
        return Observable.combineLatest(
                childRepository.getActiveChildOnce(),
                Observable.just(DateTime.now()),
                calendarRepository.getDefaultNotifyTimeInMinutes(EventType.MEDICINE_TAKING),
                (child, dateTime, minutes) -> DoctorVisit.builder()
                        .child(child)
                        .doctor(null)
                        .name(null)
                        .durationInMinutes(null)
                        .dateTime(dateTime)
                        .notifyTimeInMinutes(minutes)
                        .note(null)
                        .imageFileName(null)
                        .build());
    }

    public Observable<DoctorVisitsResponse> getDoctorVisits(@NonNull DoctorVisitsRequest request) {
        return doctorVisitRepository.getDoctorVisits(request)
                .map(doctorVisits -> DoctorVisitsResponse.builder().request(request).doctorVisits(doctorVisits).build());
    }

    public Observable<DoctorVisit> addDoctorVisit(@NonNull DoctorVisit doctorVisit) {
        return doctorVisitRepository.addDoctorVisit(doctorVisit);
    }
}
