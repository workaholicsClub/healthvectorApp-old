package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.calendar.CalendarDataRepository;
import ru.android.childdiary.data.repositories.child.ChildDataRepository;
import ru.android.childdiary.data.repositories.core.settings.SettingsDataRepository;
import ru.android.childdiary.data.repositories.medical.MedicineTakingDataRepository;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.interactors.calendar.CalendarRepository;
import ru.android.childdiary.domain.interactors.child.ChildRepository;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.core.TimeUnit;
import ru.android.childdiary.domain.interactors.core.settings.SettingsRepository;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.requests.MedicineTakingListRequest;
import ru.android.childdiary.domain.interactors.medical.requests.MedicineTakingListResponse;
import ru.android.childdiary.domain.interactors.medical.validation.MedicalValidationException;
import ru.android.childdiary.domain.interactors.medical.validation.MedicalValidationResult;
import ru.android.childdiary.domain.interactors.medical.validation.MedicineTakingValidator;
import ru.android.childdiary.presentation.core.bindings.FieldValueChangeEventsObservable;

public class MedicineTakingInteractor {
    private final ChildRepository childRepository;
    private final CalendarRepository calendarRepository;
    private final SettingsRepository settingsRepository;
    private final MedicineTakingRepository medicineTakingRepository;
    private final MedicineTakingValidator medicineTakingValidator;

    @Inject
    public MedicineTakingInteractor(ChildDataRepository childRepository,
                                    CalendarDataRepository calendarRepository,
                                    SettingsDataRepository settingsRepository,
                                    MedicineTakingDataRepository medicineTakingRepository,
                                    MedicineTakingValidator medicineTakingValidator) {
        this.childRepository = childRepository;
        this.calendarRepository = calendarRepository;
        this.settingsRepository = settingsRepository;
        this.medicineTakingRepository = medicineTakingRepository;
        this.medicineTakingValidator = medicineTakingValidator;
    }

    public Observable<List<Medicine>> getMedicines() {
        return medicineTakingRepository.getMedicines();
    }

    public Observable<Medicine> addMedicine(@NonNull Medicine medicine) {
        return medicineTakingRepository.addMedicine(medicine);
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
                        .times(new ArrayList<>(times))
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

    public Observable<MedicineTakingListResponse> getMedicineTakingList(@NonNull MedicineTakingListRequest request) {
        return medicineTakingRepository.getMedicineTakingList(request)
                .map(medicineTakingList -> MedicineTakingListResponse.builder().request(request).medicineTakingList(medicineTakingList).build());
    }

    private Observable<MedicineTaking> validate(@NonNull MedicineTaking medicineTaking) {
        return Observable.just(medicineTaking)
                .flatMap(item -> {
                    List<MedicalValidationResult> results = medicineTakingValidator.validate(item);
                    if (!medicineTakingValidator.isValid(results)) {
                        return Observable.error(new MedicalValidationException(results));
                    }
                    return Observable.just(item);
                });
    }

    public Observable<MedicineTaking> addMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return validate(medicineTaking).flatMap(medicineTakingRepository::addMedicineTaking);
    }

    public Observable<MedicineTaking> updateMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return validate(medicineTaking).flatMap(medicineTakingRepository::updateMedicineTaking);
    }

    public Observable<MedicineTaking> deleteMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return medicineTakingRepository.deleteMedicineTaking(medicineTaking);
    }

    public Observable<Boolean> controlDoneButton(
            @NonNull FieldValueChangeEventsObservable<Medicine> medicineObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return Observable.combineLatest(
                medicineObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable,
                (medicineEvent, linearGroupsEvent, periodicityTypeEvent, lengthValueEvent) -> MedicineTaking.builder()
                        .medicine(medicineEvent.getValue())
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

    public Observable<List<MedicalValidationResult>> controlFields(
            @NonNull FieldValueChangeEventsObservable<Medicine> medicineObservable,
            @NonNull FieldValueChangeEventsObservable<LinearGroups> linearGroupsObservable,
            @NonNull FieldValueChangeEventsObservable<PeriodicityType> periodicityTypeObservable,
            @NonNull FieldValueChangeEventsObservable<LengthValue> lengthValueObservable) {
        return Observable.combineLatest(
                medicineObservable,
                linearGroupsObservable,
                periodicityTypeObservable,
                lengthValueObservable,
                (medicineEvent, linearGroupsEvent, periodicityTypeEvent, lengthValueEvent) -> MedicineTaking.builder()
                        .medicine(medicineEvent.getValue())
                        .repeatParameters(RepeatParameters.builder()
                                .frequency(linearGroupsEvent.getValue())
                                .periodicity(periodicityTypeEvent.getValue())
                                .length(lengthValueEvent.getValue())
                                .build())
                        .build()
        ).map(medicineTakingValidator::validate);
    }
}
