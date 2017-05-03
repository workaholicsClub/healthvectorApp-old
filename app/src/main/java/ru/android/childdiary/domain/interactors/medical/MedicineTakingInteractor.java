package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

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

    public Observable<MedicineTaking> getDefaultOtherEvent() {
        return Observable.combineLatest(
                childRepository.getActiveChildOnce(),
                Observable.just(DateTime.now()),
                calendarRepository.getDefaultNotifyTimeInMinutes(EventType.MEDICINE_TAKING),
                (child, dateTime, minutes) -> MedicineTaking.builder()
                        .child(child)
                        .medicine(null)
                        .amount(null)
                        .medicineMeasure(null)
                        .dateTime(dateTime)
                        .notifyTimeInMinutes(minutes)
                        .note(null)
                        .imageFileName(null)
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
}
