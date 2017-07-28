package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import org.joda.time.DateTime;
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
import ru.android.childdiary.domain.core.DeleteResponse;
import ru.android.childdiary.domain.core.HasDataResponse;
import ru.android.childdiary.domain.core.validation.EventValidationException;
import ru.android.childdiary.domain.core.validation.EventValidationResult;
import ru.android.childdiary.domain.interactors.calendar.CalendarRepository;
import ru.android.childdiary.domain.interactors.child.Child;
import ru.android.childdiary.domain.interactors.child.ChildRepository;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.core.TimeUnit;
import ru.android.childdiary.domain.interactors.core.images.ImageType;
import ru.android.childdiary.domain.interactors.core.images.ImagesRepository;
import ru.android.childdiary.domain.interactors.core.settings.SettingsRepository;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.requests.CompleteMedicineTakingRequest;
import ru.android.childdiary.domain.interactors.medical.requests.CompleteMedicineTakingResponse;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingEventsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingEventsResponse;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingResponse;
import ru.android.childdiary.domain.interactors.medical.requests.GetMedicineTakingListFilter;
import ru.android.childdiary.domain.interactors.medical.requests.GetMedicineTakingListRequest;
import ru.android.childdiary.domain.interactors.medical.requests.GetMedicineTakingListResponse;
import ru.android.childdiary.domain.interactors.medical.requests.UpsertMedicineTakingRequest;
import ru.android.childdiary.domain.interactors.medical.requests.UpsertMedicineTakingResponse;
import ru.android.childdiary.domain.interactors.medical.validation.MedicineTakingValidator;
import ru.android.childdiary.domain.interactors.medical.validation.MedicineValidator;
import ru.android.childdiary.presentation.core.bindings.FieldValueChangeEventsObservable;

public class MedicineTakingInteractor implements MedicalDictionaryInteractor<Medicine> {
    private final ChildRepository childRepository;
    private final CalendarRepository calendarRepository;
    private final SettingsRepository settingsRepository;
    private final MedicineTakingRepository medicineTakingRepository;
    private final MedicineTakingValidator medicineTakingValidator;
    private final ImagesRepository imagesRepository;
    private final MedicineTakingFilterDataRepository filterRepository;
    private final MedicineValidator medicineValidator;

    @Inject
    public MedicineTakingInteractor(ChildDataRepository childRepository,
                                    CalendarDataRepository calendarRepository,
                                    SettingsDataRepository settingsRepository,
                                    MedicineTakingDataRepository medicineTakingRepository,
                                    MedicineTakingValidator medicineTakingValidator,
                                    ImagesDataRepository imagesRepository,
                                    MedicineTakingFilterDataRepository filterRepository,
                                    MedicineValidator medicineValidator) {
        this.childRepository = childRepository;
        this.calendarRepository = calendarRepository;
        this.settingsRepository = settingsRepository;
        this.medicineTakingRepository = medicineTakingRepository;
        this.medicineTakingValidator = medicineTakingValidator;
        this.imagesRepository = imagesRepository;
        this.filterRepository = filterRepository;
        this.medicineValidator = medicineValidator;
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

    public Observable<List<Medicine>> getMedicines() {
        return medicineTakingRepository.getMedicines();
    }

    public Observable<Medicine> addMedicine(@NonNull Medicine medicine) {
        return validate(medicine).flatMap(medicineTakingRepository::addMedicine);
    }

    private Observable<Medicine> validate(@NonNull Medicine medicine) {
        return Observable.defer(() -> {
            List<EventValidationResult> results = medicineValidator.validate(medicine);
            if (!medicineValidator.isValid(results)) {
                return Observable.error(new EventValidationException(results));
            }
            return Observable.just(medicine);
        });
    }

    public Observable<Medicine> deleteMedicine(@NonNull Medicine medicine) {
        return medicineTakingRepository.deleteMedicine(medicine);
    }

    public Observable<MedicineTaking> getDefaultMedicineTaking() {
        return Observable.combineLatest(
                childRepository.getActiveChildOnce(),
                settingsRepository.getStartTimeOnce()
                        .map(Collections::singletonList)
                        .map(this::getDefaultRepeatParameters),
                Observable.just(DateTime.now()),
                calendarRepository.getDefaultNotifyTimeInMinutes(EventType.MEDICINE_TAKING),
                (child, repeatParameters, dateTime, minutes) -> MedicineTaking.builder()
                        .child(child)
                        .medicine(null)
                        .amount(null)
                        .medicineMeasure(null)
                        .repeatParameters(repeatParameters)
                        .dateTime(dateTime)
                        .finishDateTime(null)
                        .isExported(true)
                        .notifyTimeInMinutes(minutes)
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

    public Observable<List<MedicineMeasure>> getMedicineMeasureList() {
        return medicineTakingRepository.getMedicineMeasureList();
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
        return Observable.defer(() -> {
            List<EventValidationResult> results = medicineTakingValidator.validate(request.getMedicineTaking());
            if (!medicineTakingValidator.isValid(results)) {
                return Observable.error(new EventValidationException(results));
            }
            return Observable.just(request);
        });
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
                .map(medicineTakingValidator::validate)
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
                .map(medicineTakingValidator::validate);
    }

    @Override
    public Observable<Boolean> controlDoneButton(@NonNull Observable<TextViewAfterTextChangeEvent> nameObservable) {
        return nameObservable
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Editable::toString)
                .map(String::trim)
                .map(name -> !TextUtils.isEmpty(name))
                .distinctUntilChanged();
    }

    public Observable<List<EventValidationResult>> controlFields(@NonNull Observable<TextViewAfterTextChangeEvent> nameObservable) {
        return nameObservable
                .map(TextViewAfterTextChangeEvent::editable)
                .map(Editable::toString)
                .map(String::trim)
                .map(name -> Medicine.builder().name(name).build())
                .map(medicineValidator::validate);
    }
}
