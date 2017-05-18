package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;
import android.text.Editable;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.calendar.CalendarDataRepository;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.data.repositories.core.images.ImagesDataRepository;
import ru.android.childdiary.data.repositories.core.settings.SettingsDataRepository;
import ru.android.childdiary.data.repositories.medical.DoctorVisitDataRepository;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.core.DeleteResponse;
import ru.android.childdiary.domain.interactors.calendar.CalendarRepository;
import ru.android.childdiary.domain.interactors.child.ChildRepository;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.core.images.ImageType;
import ru.android.childdiary.domain.interactors.core.images.ImagesRepository;
import ru.android.childdiary.domain.interactors.core.settings.SettingsRepository;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
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
import ru.android.childdiary.domain.interactors.medical.validation.DoctorVisitValidator;
import ru.android.childdiary.domain.interactors.medical.validation.MedicalValidationException;
import ru.android.childdiary.domain.interactors.medical.validation.MedicalValidationResult;
import ru.android.childdiary.presentation.core.bindings.FieldValueChangeEventsObservable;

public class DoctorVisitInteractor {
    private final ChildRepository childRepository;
    private final CalendarRepository calendarRepository;
    private final SettingsRepository settingsRepository;
    private final DoctorVisitRepository doctorVisitRepository;
    private final DoctorVisitValidator doctorVisitValidator;
    private final ImagesRepository imagesRepository;

    @Inject
    public DoctorVisitInteractor(ChildDataRepository childRepository,
                                 CalendarDataRepository calendarRepository,
                                 SettingsDataRepository settingsRepository,
                                 DoctorVisitDataRepository doctorVisitRepository,
                                 DoctorVisitValidator doctorVisitValidator,
                                 ImagesDataRepository imagesRepository) {
        this.childRepository = childRepository;
        this.calendarRepository = calendarRepository;
        this.settingsRepository = settingsRepository;
        this.doctorVisitRepository = doctorVisitRepository;
        this.doctorVisitValidator = doctorVisitValidator;
        this.imagesRepository = imagesRepository;
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
                doctorVisitRepository.getLastDoctor(),
                getDefaultRepeatParameters(),
                Observable.just(DateTime.now()),
                calendarRepository.getDefaultNotifyTimeInMinutes(EventType.DOCTOR_VISIT),
                (child, doctor, repeatParameters, dateTime, minutes) -> DoctorVisit.builder()
                        .child(child)
                        .doctor(doctor)
                        .repeatParameters(repeatParameters)
                        .name(null)
                        .durationInMinutes(15)
                        .dateTime(dateTime)
                        .finishDateTime(null)
                        .isExported(true)
                        .notifyTimeInMinutes(minutes)
                        .note(null)
                        .imageFileName(null)
                        .isDeleted(null)
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

    public Observable<LocalTime> getStartTimeOnce() {
        return settingsRepository.getStartTimeOnce();
    }

    public Observable<LocalTime> getFinishTimeOnce() {
        return settingsRepository.getFinishTimeOnce();
    }

    public Observable<GetDoctorVisitsResponse> getDoctorVisits(@NonNull GetDoctorVisitsRequest request) {
        return doctorVisitRepository.getDoctorVisits(request);
    }

    private Observable<UpsertDoctorVisitRequest> validate(@NonNull UpsertDoctorVisitRequest request) {
        DoctorVisit doctorVisit = request.getDoctorVisit();
        return Observable.defer(() -> {
            List<MedicalValidationResult> results = doctorVisitValidator.validate(request.getDoctorVisit());
            if (!doctorVisitValidator.isValid(results)) {
                return Observable.error(new MedicalValidationException(results));
            }
            return Observable.just(request);
        });
    }

    private Observable<UpsertDoctorVisitRequest> createImageFile(@NonNull UpsertDoctorVisitRequest request) {
        return Observable.fromCallable(() -> {
            DoctorVisit doctorVisit = request.getDoctorVisit();
            if (imagesRepository.isTemporaryImageFile(doctorVisit.getImageFileName())) {
                String uniqueImageFileName = imagesRepository.createUniqueImageFileRelativePath(ImageType.DOCTOR_VISIT, doctorVisit.getImageFileName());
                doctorVisit = doctorVisit.toBuilder().imageFileName(uniqueImageFileName).build();
                return request.toBuilder().doctorVisit(doctorVisit).build();
            }
            return request;
        });
    }

    public Observable<UpsertDoctorVisitResponse> addDoctorVisit(@NonNull UpsertDoctorVisitRequest request) {
        return validate(request)
                .flatMap(this::createImageFile)
                .flatMap(doctorVisitRepository::addDoctorVisit)
                .flatMap(this::postprocess);
    }

    public Observable<UpsertDoctorVisitResponse> updateDoctorVisit(@NonNull UpsertDoctorVisitRequest request) {
        return validate(request)
                .flatMap(this::createImageFile)
                .flatMap(doctorVisitRepository::updateDoctorVisit)
                .flatMap(this::postprocess);
    }

    private Observable<UpsertDoctorVisitResponse> postprocess(@NonNull UpsertDoctorVisitResponse response) {
        return Observable.fromCallable(() -> {
            doctorVisitRepository.setLastDoctor(response.getDoctorVisit().getDoctor());
            return response;
        });
    }

    private <T extends DeleteResponse> Observable<T> deleteImageFiles(@NonNull T response) {
        return Observable.fromCallable(() -> {
            imagesRepository.deleteImageFiles(response.getImageFilesToDelete());
            return response;
        });
    }

    public Observable<DeleteDoctorVisitResponse> deleteDoctorVisit(@NonNull DeleteDoctorVisitRequest request) {
        return doctorVisitRepository.deleteDoctorVisit(request)
                .flatMap(this::deleteImageFiles);
    }

    public Observable<DeleteDoctorVisitEventsResponse> deleteDoctorVisitWithEvents(@NonNull DeleteDoctorVisitEventsRequest request) {
        return doctorVisitRepository.deleteDoctorVisitWithEvents(request)
                .flatMap(this::deleteImageFiles);
    }

    public Observable<CompleteDoctorVisitResponse> completeDoctorVisit(@NonNull CompleteDoctorVisitRequest request) {
        return doctorVisitRepository.completeDoctorVisit(request)
                .flatMap(this::deleteImageFiles);
    }

    public Observable<Boolean> controlDoneButton(
            @NonNull Observable<TextViewAfterTextChangeEvent> doctorVisitNameObservable,
            @NonNull FieldValueChangeEventsObservable<Doctor> doctorObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return Observable.combineLatest(
                doctorVisitNameObservable
                        .map(TextViewAfterTextChangeEvent::editable)
                        .map(Editable::toString),
                doctorObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable,
                (doctorVisitName, doctorEvent, linearGroupsEvent, periodicityTypeEvent, lengthValueEvent) -> DoctorVisit.builder()
                        .name(doctorVisitName)
                        .doctor(doctorEvent.getValue())
                        .repeatParameters(RepeatParameters.builder()
                                .frequency(linearGroupsEvent.getValue())
                                .periodicity(periodicityTypeEvent.getValue())
                                .length(lengthValueEvent.getValue())
                                .build())
                        .build())
                .map(doctorVisitValidator::validate)
                .map(doctorVisitValidator::isValid)
                .distinctUntilChanged();
    }

    public Observable<List<MedicalValidationResult>> controlFields(
            @NonNull Observable<TextViewAfterTextChangeEvent> doctorVisitNameObservable,
            @NonNull FieldValueChangeEventsObservable<Doctor> doctorObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return Observable.combineLatest(
                doctorVisitNameObservable
                        .map(TextViewAfterTextChangeEvent::editable)
                        .map(Editable::toString),
                doctorObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable,
                (doctorVisitName, doctorEvent, linearGroupsEvent, periodicityTypeEvent, lengthValueEvent) -> DoctorVisit.builder()
                        .name(doctorVisitName)
                        .doctor(doctorEvent.getValue())
                        .repeatParameters(RepeatParameters.builder()
                                .frequency(linearGroupsEvent.getValue())
                                .periodicity(periodicityTypeEvent.getValue())
                                .length(lengthValueEvent.getValue())
                                .build())
                        .build()
        ).map(doctorVisitValidator::validate);
    }
}
