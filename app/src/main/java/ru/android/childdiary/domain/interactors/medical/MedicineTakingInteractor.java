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
import ru.android.childdiary.data.repositories.medical.MedicineTakingDataRepository;
import ru.android.childdiary.data.types.EventType;
import ru.android.childdiary.domain.core.Interactor;
import ru.android.childdiary.domain.interactors.calendar.CalendarRepository;
import ru.android.childdiary.domain.interactors.child.ChildRepository;
import ru.android.childdiary.domain.interactors.core.LengthValue;
import ru.android.childdiary.domain.interactors.core.LinearGroups;
import ru.android.childdiary.domain.interactors.core.PeriodicityType;
import ru.android.childdiary.domain.interactors.core.RepeatParameters;
import ru.android.childdiary.domain.interactors.core.TimeUnit;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.requests.MedicineTakingListRequest;
import ru.android.childdiary.domain.interactors.medical.requests.MedicineTakingListResponse;

public class MedicineTakingInteractor implements Interactor {
    private final ChildRepository childRepository;
    private final CalendarRepository calendarRepository;
    private final MedicineTakingRepository medicineTakingRepository;

    @Inject
    public MedicineTakingInteractor(ChildDataRepository childRepository,
                                    CalendarDataRepository calendarRepository,
                                    MedicineTakingDataRepository medicineTakingRepository) {
        this.childRepository = childRepository;
        this.calendarRepository = calendarRepository;
        this.medicineTakingRepository = medicineTakingRepository;
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
                getDefaultRepeatParameters(),
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
                        .exported(null)
                        .notifyTimeInMinutes(minutes)
                        .note(null)
                        .imageFileName(null)
                        .build());
    }

    private Observable<RepeatParameters> getDefaultRepeatParameters() {
        return Observable.just(
                RepeatParameters.builder()
                        .frequency(LinearGroups.builder()
                                .times(new ArrayList<>(Collections.singletonList(LocalTime.MIDNIGHT)))
                                .build())
                        .periodicity(PeriodicityType.DAILY)
                        .length(LengthValue.builder()
                                .length(1)
                                .timeUnit(TimeUnit.WEEK)
                                .build())
                        .build());
    }

    public Observable<List<MedicineMeasure>> getMedicineMeasureList() {
        return medicineTakingRepository.getMedicineMeasureList();
    }

    public Observable<MedicineTakingListResponse> getMedicineTakingList(@NonNull MedicineTakingListRequest request) {
        return medicineTakingRepository.getMedicineTakingList(request)
                .map(medicineTakingList -> MedicineTakingListResponse.builder().request(request).medicineTakingList(medicineTakingList).build());
    }

    public Observable<MedicineTaking> addMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return medicineTakingRepository.addMedicineTaking(medicineTaking);
    }

    public Observable<MedicineTaking> updateMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return medicineTakingRepository.updateMedicineTaking(medicineTaking);
    }

    public Observable<MedicineTaking> deleteMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return medicineTakingRepository.deleteMedicineTaking(medicineTaking);
    }
}
