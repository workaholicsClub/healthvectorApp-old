package ru.android.childdiary.domain.medical;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.data.repositories.calendar.CalendarDataRepository;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.data.repositories.core.images.ImagesDataRepository;
import ru.android.childdiary.data.repositories.core.settings.SettingsDataRepository;
import ru.android.childdiary.data.repositories.medical.MedicineTakingDataRepository;
import ru.android.childdiary.data.repositories.medical.MedicineTakingFilterDataRepository;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.calendar.CalendarRepository;
import ru.android.childdiary.domain.calendar.data.core.LengthValue;
import ru.android.childdiary.domain.calendar.data.core.LinearGroups;
import ru.android.childdiary.domain.calendar.data.core.PeriodicityType;
import ru.android.childdiary.domain.calendar.data.core.RepeatParameters;
import ru.android.childdiary.domain.calendar.data.core.TimeUnit;
import ru.android.childdiary.domain.child.ChildRepository;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.core.ValueRepository;
import ru.android.childdiary.domain.core.images.ImageType;
import ru.android.childdiary.domain.core.images.ImagesRepository;
import ru.android.childdiary.domain.core.requests.DeleteResponse;
import ru.android.childdiary.domain.core.requests.HasDataResponse;
import ru.android.childdiary.domain.core.settings.SettingsRepository;
import ru.android.childdiary.domain.core.validation.EventValidationResult;
import ru.android.childdiary.domain.dictionaries.medicines.data.Medicine;
import ru.android.childdiary.domain.medical.data.MedicineTaking;
import ru.android.childdiary.domain.medical.requests.CompleteMedicineTakingRequest;
import ru.android.childdiary.domain.medical.requests.CompleteMedicineTakingResponse;
import ru.android.childdiary.domain.medical.requests.DeleteMedicineTakingEventsRequest;
import ru.android.childdiary.domain.medical.requests.DeleteMedicineTakingEventsResponse;
import ru.android.childdiary.domain.medical.requests.DeleteMedicineTakingRequest;
import ru.android.childdiary.domain.medical.requests.DeleteMedicineTakingResponse;
import ru.android.childdiary.domain.medical.requests.GetMedicineTakingListFilter;
import ru.android.childdiary.domain.medical.requests.GetMedicineTakingListRequest;
import ru.android.childdiary.domain.medical.requests.GetMedicineTakingListResponse;
import ru.android.childdiary.domain.medical.requests.UpsertMedicineTakingRequest;
import ru.android.childdiary.domain.medical.requests.UpsertMedicineTakingResponse;
import ru.android.childdiary.domain.medical.validation.MedicineTakingValidator;
import ru.android.childdiary.presentation.core.bindings.FieldValueChangeEventsObservable;

public class MedicineTakingInteractor {
    private final ChildRepository childRepository;
    private final CalendarRepository calendarRepository;
    private final SettingsRepository settingsRepository;
    private final MedicineTakingRepository medicineTakingRepository;
    private final MedicineTakingValidator medicineTakingValidator;
    private final ImagesRepository imagesRepository;
    private final ValueRepository<GetMedicineTakingListFilter> filterRepository;

    @Inject
    public MedicineTakingInteractor(ChildDataRepository childRepository,
                                    CalendarDataRepository calendarRepository,
                                    SettingsDataRepository settingsRepository,
                                    MedicineTakingDataRepository medicineTakingRepository,
                                    MedicineTakingValidator medicineTakingValidator,
                                    ImagesDataRepository imagesRepository,
                                    MedicineTakingFilterDataRepository filterRepository) {
        this.childRepository = childRepository;
        this.calendarRepository = calendarRepository;
        this.settingsRepository = settingsRepository;
        this.medicineTakingRepository = medicineTakingRepository;
        this.medicineTakingValidator = medicineTakingValidator;
        this.imagesRepository = imagesRepository;
        this.filterRepository = filterRepository;
    }

    public Observable<GetMedicineTakingListFilter> getSelectedFilterValue() {
        return filterRepository.getSelectedValue();
    }

    public void setSelectedFilterValue(@NonNull GetMedicineTakingListFilter value) {
        filterRepository.setSelectedValue(value);
    }

    public Observable<GetMedicineTakingListFilter> getSelectedFilterValueOnce() {
        return filterRepository.getSelectedValueOnce();
    }

    public Observable<GetMedicineTakingListFilter> setSelectedFilterValueObservable(@NonNull GetMedicineTakingListFilter value) {
        return filterRepository.setSelectedValueObservable(value);
    }

    public Observable<MedicineTaking> getDefaultMedicineTaking() {
        EventType eventType = EventType.MEDICINE_TAKING;
        return Observable.combineLatest(
                childRepository.getActiveChildOnce(),
                settingsRepository.getStartTimeOnce()
                        .map(Collections::singletonList)
                        .map(this::getDefaultRepeatParameters),
                Observable.just(DateTime.now()),
                calendarRepository.getNotificationSettingsOnce(eventType),
                (child, repeatParameters, dateTime, eventNotification) -> MedicineTaking.builder()
                        .child(child)
                        .medicine(null)
                        .amount(null)
                        .medicineMeasure(null)
                        .repeatParameters(repeatParameters)
                        .dateTime(dateTime)
                        .finishDateTime(null)
                        .isExported(true)
                        .notifyTimeInMinutes(eventNotification.getNotifyTime())
                        .note(null)
                        .imageFileName(null)
                        .isDeleted(null)
                        .build());
    }

    private RepeatParameters getDefaultRepeatParameters(@NonNull List<LocalTime> times) {
        return RepeatParameters.builder()
                .frequency(LinearGroups.builder()
                        .times(times)
                        .build())
                .periodicity(PeriodicityType.DAILY)
                .length(LengthValue.builder()
                        .length(1)
                        .timeUnit(TimeUnit.WEEK)
                        .build())
                .build();
    }

    public Observable<LocalTime> getStartTimeOnce() {
        return settingsRepository.getStartTimeOnce();
    }

    public Observable<LocalTime> getFinishTimeOnce() {
        return settingsRepository.getFinishTimeOnce();
    }

    public Single<Boolean> hasConnectedEvents(@NonNull MedicineTaking medicineTaking) {
        return medicineTakingRepository.hasConnectedEvents(medicineTaking);
    }

    public Single<HasDataResponse> hasDataToFilter(@NonNull Child child) {
        return medicineTakingRepository.hasDataToFilter(child)
                .map(hasData -> HasDataResponse.builder()
                        .child(child)
                        .hasData(hasData)
                        .build());
    }

    public Observable<GetMedicineTakingListResponse> getMedicineTakingList(@NonNull GetMedicineTakingListRequest request) {
        return medicineTakingRepository.getMedicineTakingList(request);
    }

    private Observable<UpsertMedicineTakingRequest> validate(@NonNull UpsertMedicineTakingRequest request) {
        return medicineTakingValidator.validateObservable(request.getMedicineTaking())
                .map(medicineTaking -> request.toBuilder().medicineTaking(medicineTaking).build());
    }

    private Observable<UpsertMedicineTakingRequest> createImageFile(@NonNull UpsertMedicineTakingRequest request) {
        return Observable.fromCallable(() -> {
            MedicineTaking medicineTaking = request.getMedicineTaking();
            if (imagesRepository.isTemporaryImageFile(medicineTaking.getImageFileName())) {
                String uniqueImageFileName = imagesRepository.createUniqueImageFileRelativePath(ImageType.MEDICINE_TAKING, medicineTaking.getImageFileName());
                medicineTaking = medicineTaking.toBuilder().imageFileName(uniqueImageFileName).build();
                return request.toBuilder().medicineTaking(medicineTaking).build();
            }
            return request;
        });
    }

    public Observable<UpsertMedicineTakingResponse> addMedicineTaking(@NonNull UpsertMedicineTakingRequest request) {
        return validate(request)
                .flatMap(this::createImageFile)
                .flatMap(medicineTakingRepository::addMedicineTaking);
    }

    public Observable<UpsertMedicineTakingResponse> updateMedicineTaking(@NonNull UpsertMedicineTakingRequest request) {
        return validate(request)
                .flatMap(this::createImageFile)
                .flatMap(medicineTakingRepository::updateMedicineTaking)
                .flatMap(this::deleteImageFiles);
    }

    private <T extends DeleteResponse> Observable<T> deleteImageFiles(@NonNull T response) {
        return Observable.fromCallable(() -> {
            imagesRepository.deleteImageFiles(response.getImageFilesToDelete());
            return response;
        });
    }

    public Observable<DeleteMedicineTakingResponse> deleteMedicineTaking(@NonNull DeleteMedicineTakingRequest request) {
        return medicineTakingRepository.deleteMedicineTaking(request)
                .flatMap(this::deleteImageFiles);
    }

    public Observable<DeleteMedicineTakingEventsResponse> deleteMedicineTakingWithEvents(@NonNull DeleteMedicineTakingEventsRequest request) {
        return medicineTakingRepository.deleteMedicineTakingWithEvents(request)
                .flatMap(this::deleteImageFiles);
    }

    public Observable<CompleteMedicineTakingResponse> completeMedicineTaking(@NonNull CompleteMedicineTakingRequest request) {
        return medicineTakingRepository.completeMedicineTaking(request)
                .flatMap(this::deleteImageFiles);
    }

    public Observable<Boolean> controlDoneButton(
            @NonNull FieldValueChangeEventsObservable<Medicine> medicineObservable,
            @NonNull FieldValueChangeEventsObservable<Boolean> exportedObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return Observable.combineLatest(
                medicineObservable,
                exportedObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable,
                (medicineEvent, exportedEvent, linearGroupsEvent, periodicityTypeEvent, lengthValueEvent) -> MedicineTaking.builder()
                        .medicine(medicineEvent.getValue())
                        .isExported(exportedEvent.getValue())
                        .repeatParameters(RepeatParameters.builder()
                                .frequency(linearGroupsEvent.getValue())
                                .periodicity(periodicityTypeEvent.getValue())
                                .length(lengthValueEvent.getValue())
                                .build())
                        .build())
                .map(medicineTakingValidator::validateOnUi)
                .map(medicineTakingValidator::isValid)
                .distinctUntilChanged();
    }

    public Observable<List<EventValidationResult>> controlFields(
            @NonNull FieldValueChangeEventsObservable<Medicine> medicineObservable,
            @NonNull FieldValueChangeEventsObservable<Boolean> exportedObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return Observable.combineLatest(
                medicineObservable,
                exportedObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable,
                (medicineEvent, exportedEvent, linearGroupsEvent, periodicityTypeEvent, lengthValueEvent) -> MedicineTaking.builder()
                        .medicine(medicineEvent.getValue())
                        .isExported(exportedEvent.getValue())
                        .repeatParameters(RepeatParameters.builder()
                                .frequency(linearGroupsEvent.getValue())
                                .periodicity(periodicityTypeEvent.getValue())
                                .length(lengthValueEvent.getValue())
                                .build())
                        .build())
                .map(medicineTakingValidator::validateOnUi);
    }

    public Observable<Integer> continueLinearGroup(@NonNull MedicineTaking medicineTaking,
                                                   @NonNull LocalDate sinceDate,
                                                   @NonNull Integer linearGroup,
                                                   @NonNull LengthValue lengthValue) {
        return medicineTakingRepository.continueLinearGroup(medicineTaking, sinceDate, linearGroup, lengthValue);
    }
}
