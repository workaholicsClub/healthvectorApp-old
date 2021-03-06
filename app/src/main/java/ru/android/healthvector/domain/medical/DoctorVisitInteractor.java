package ru.android.healthvector.domain.medical;

import android.support.annotation.NonNull;
import android.text.Editable;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.healthvector.data.repositories.calendar.CalendarDataRepository;
import ru.android.healthvector.data.repositories.child.ChildDataRepository;
import ru.android.healthvector.data.repositories.core.images.ImagesDataRepository;
import ru.android.healthvector.data.repositories.core.settings.SettingsDataRepository;
import ru.android.healthvector.data.repositories.medical.DoctorVisitDataRepository;
import ru.android.healthvector.data.repositories.medical.DoctorVisitFilterDataRepository;
import ru.android.healthvector.data.types.EventType;
import ru.android.healthvector.domain.calendar.CalendarRepository;
import ru.android.healthvector.domain.calendar.data.core.LengthValue;
import ru.android.healthvector.domain.calendar.data.core.LinearGroups;
import ru.android.healthvector.domain.calendar.data.core.PeriodicityType;
import ru.android.healthvector.domain.calendar.data.core.RepeatParameters;
import ru.android.healthvector.domain.child.ChildRepository;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.core.ValueRepository;
import ru.android.healthvector.domain.core.images.ImageType;
import ru.android.healthvector.domain.core.images.ImagesRepository;
import ru.android.healthvector.domain.core.requests.DeleteResponse;
import ru.android.healthvector.domain.core.requests.HasDataResponse;
import ru.android.healthvector.domain.core.settings.SettingsRepository;
import ru.android.healthvector.domain.core.validation.EventValidationResult;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;
import ru.android.healthvector.domain.medical.data.DoctorVisit;
import ru.android.healthvector.domain.medical.requests.CompleteDoctorVisitRequest;
import ru.android.healthvector.domain.medical.requests.CompleteDoctorVisitResponse;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitEventsRequest;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitEventsResponse;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitRequest;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitResponse;
import ru.android.healthvector.domain.medical.requests.GetDoctorVisitsFilter;
import ru.android.healthvector.domain.medical.requests.GetDoctorVisitsRequest;
import ru.android.healthvector.domain.medical.requests.GetDoctorVisitsResponse;
import ru.android.healthvector.domain.medical.requests.UpsertDoctorVisitRequest;
import ru.android.healthvector.domain.medical.requests.UpsertDoctorVisitResponse;
import ru.android.healthvector.domain.medical.validation.DoctorVisitValidator;
import ru.android.healthvector.presentation.core.bindings.FieldValueChangeEventsObservable;

public class DoctorVisitInteractor {
    private final ChildRepository childRepository;
    private final CalendarRepository calendarRepository;
    private final SettingsRepository settingsRepository;
    private final DoctorVisitRepository doctorVisitRepository;
    private final DoctorVisitValidator doctorVisitValidator;
    private final ImagesRepository imagesRepository;
    private final ValueRepository<GetDoctorVisitsFilter> filterRepository;

    @Inject
    public DoctorVisitInteractor(ChildDataRepository childRepository,
                                 CalendarDataRepository calendarRepository,
                                 SettingsDataRepository settingsRepository,
                                 DoctorVisitDataRepository doctorVisitRepository,
                                 DoctorVisitValidator doctorVisitValidator,
                                 ImagesDataRepository imagesRepository,
                                 DoctorVisitFilterDataRepository filterRepository) {
        this.childRepository = childRepository;
        this.calendarRepository = calendarRepository;
        this.settingsRepository = settingsRepository;
        this.doctorVisitRepository = doctorVisitRepository;
        this.doctorVisitValidator = doctorVisitValidator;
        this.imagesRepository = imagesRepository;
        this.filterRepository = filterRepository;
    }

    public Observable<GetDoctorVisitsFilter> getSelectedFilterValue() {
        return filterRepository.getSelectedValue();
    }

    public void setSelectedFilterValue(@NonNull GetDoctorVisitsFilter value) {
        filterRepository.setSelectedValue(value);
    }

    public Observable<GetDoctorVisitsFilter> getSelectedFilterValueOnce() {
        return filterRepository.getSelectedValueOnce();
    }

    public Observable<GetDoctorVisitsFilter> setSelectedFilterValueObservable(@NonNull GetDoctorVisitsFilter value) {
        return filterRepository.setSelectedValueObservable(value);
    }

    public Observable<DoctorVisit> getDefaultDoctorVisit() {
        EventType eventType = EventType.DOCTOR_VISIT;
        return Observable.combineLatest(
                childRepository.getActiveChildOnce(),
                doctorVisitRepository.getLastDoctor(),
                getDefaultRepeatParameters(),
                Observable.just(DateTime.now()),
                calendarRepository.getNotificationSettingsOnce(eventType),
                (child, doctor, repeatParameters, dateTime, eventNotification) -> DoctorVisit.builder()
                        .child(child)
                        .doctor(doctor)
                        .repeatParameters(repeatParameters)
                        .name(null)
                        .durationInMinutes(15)
                        .dateTime(dateTime)
                        .finishDateTime(null)
                        .isExported(true)
                        .notifyTimeInMinutes(eventNotification.getNotifyTime())
                        .note(null)
                        .imageFileName(null)
                        .isDeleted(null)
                        .build());
    }

    private Observable<RepeatParameters> getDefaultRepeatParameters() {
        return Observable.just(
                RepeatParameters.builder()
                        .frequency(LinearGroups.builder()
                                .times(Collections.emptyList())
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

    public Single<Boolean> hasConnectedEvents(@NonNull DoctorVisit doctorVisit) {
        return doctorVisitRepository.hasConnectedEvents(doctorVisit);
    }

    public Single<HasDataResponse> hasDataToFilter(@NonNull Child child) {
        return doctorVisitRepository.hasDataToFilter(child)
                .map(hasData -> HasDataResponse.builder()
                        .child(child)
                        .hasData(hasData)
                        .build());
    }

    private Observable<UpsertDoctorVisitRequest> validate(@NonNull UpsertDoctorVisitRequest request) {
        return doctorVisitValidator.validateObservable(request.getDoctorVisit())
                .map(doctorVisit -> request.toBuilder().doctorVisit(doctorVisit).build());
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
                .flatMap(this::postprocess)
                .flatMap(this::deleteImageFiles);
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
            @NonNull FieldValueChangeEventsObservable<Boolean> exportedObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return Observable.combineLatest(
                doctorVisitNameObservable
                        .map(TextViewAfterTextChangeEvent::editable)
                        .map(Editable::toString)
                        .map(String::trim),
                doctorObservable,
                exportedObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable,
                (doctorVisitName, doctorEvent, exportedEvent, linearGroupsEvent, periodicityTypeEvent, lengthValueEvent) -> DoctorVisit.builder()
                        .name(doctorVisitName)
                        .isExported(exportedEvent.getValue())
                        .doctor(doctorEvent.getValue())
                        .repeatParameters(RepeatParameters.builder()
                                .frequency(linearGroupsEvent.getValue())
                                .periodicity(periodicityTypeEvent.getValue())
                                .length(lengthValueEvent.getValue())
                                .build())
                        .build())
                .map(doctorVisitValidator::validateOnUi)
                .map(doctorVisitValidator::isValid)
                .distinctUntilChanged();
    }

    public Observable<List<EventValidationResult>> controlFields(
            @NonNull Observable<TextViewAfterTextChangeEvent> doctorVisitNameObservable,
            @NonNull FieldValueChangeEventsObservable<Doctor> doctorObservable,
            @NonNull FieldValueChangeEventsObservable<Boolean> exportedObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return Observable.combineLatest(
                doctorVisitNameObservable
                        .map(TextViewAfterTextChangeEvent::editable)
                        .map(Editable::toString)
                        .map(String::trim),
                doctorObservable,
                exportedObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable,
                (doctorVisitName, doctorEvent, exportedEvent, linearGroupsEvent, periodicityTypeEvent, lengthValueEvent) -> DoctorVisit.builder()
                        .name(doctorVisitName)
                        .doctor(doctorEvent.getValue())
                        .isExported(exportedEvent.getValue())
                        .repeatParameters(RepeatParameters.builder()
                                .frequency(linearGroupsEvent.getValue())
                                .periodicity(periodicityTypeEvent.getValue())
                                .length(lengthValueEvent.getValue())
                                .build())
                        .build())
                .map(doctorVisitValidator::validateOnUi);
    }

    public Observable<Integer> continueLinearGroup(@NonNull DoctorVisit doctorVisit,
                                                   @NonNull LocalDate sinceDate,
                                                   @NonNull Integer linearGroup,
                                                   @NonNull LengthValue lengthValue) {
        return doctorVisitRepository.continueLinearGroup(doctorVisit, sinceDate, linearGroup, lengthValue);
    }
}
