package ru.android.childdiary.data.repositories.medical;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.data.repositories.calendar.CleanUpDbService;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.medical.MedicineTakingRepository;
import ru.android.childdiary.domain.medical.data.MedicineTaking;
import ru.android.childdiary.domain.medical.requests.CompleteMedicineTakingRequest;
import ru.android.childdiary.domain.medical.requests.CompleteMedicineTakingResponse;
import ru.android.childdiary.domain.medical.requests.DeleteMedicineTakingEventsRequest;
import ru.android.childdiary.domain.medical.requests.DeleteMedicineTakingEventsResponse;
import ru.android.childdiary.domain.medical.requests.DeleteMedicineTakingRequest;
import ru.android.childdiary.domain.medical.requests.DeleteMedicineTakingResponse;
import ru.android.childdiary.domain.medical.requests.GetMedicineTakingListRequest;
import ru.android.childdiary.domain.medical.requests.GetMedicineTakingListResponse;
import ru.android.childdiary.domain.medical.requests.UpsertMedicineTakingRequest;
import ru.android.childdiary.domain.medical.requests.UpsertMedicineTakingResponse;

@Singleton
public class MedicineTakingDataRepository implements MedicineTakingRepository {
    private final MedicineTakingDbService medicineTakingDbService;
    private final CleanUpDbService cleanUpDbService;

    @Inject
    public MedicineTakingDataRepository(MedicineTakingDbService medicineTakingDbService,
                                        CleanUpDbService cleanUpDbService) {
        this.medicineTakingDbService = medicineTakingDbService;
        this.cleanUpDbService = cleanUpDbService;
    }

    @Override
    public Observable<GetMedicineTakingListResponse> getMedicineTakingList(@NonNull GetMedicineTakingListRequest request) {
        return medicineTakingDbService.getMedicineTakingList(request);
    }

    @Override
    public Single<Boolean> hasConnectedEvents(@NonNull MedicineTaking medicineTaking) {
        return medicineTakingDbService.hasConnectedEvents(medicineTaking);
    }

    @Override
    public Single<Boolean> hasDataToFilter(@NonNull Child child) {
        return medicineTakingDbService.hasDataToFilter(child);
    }

    @Override
    public Observable<UpsertMedicineTakingResponse> addMedicineTaking(@NonNull UpsertMedicineTakingRequest request) {
        return medicineTakingDbService.add(request);
    }

    @Override
    public Observable<UpsertMedicineTakingResponse> updateMedicineTaking(@NonNull UpsertMedicineTakingRequest request) {
        return medicineTakingDbService.update(request);
    }

    @Override
    public Observable<DeleteMedicineTakingResponse> deleteMedicineTaking(@NonNull DeleteMedicineTakingRequest request) {
        return cleanUpDbService.deleteMedicineTaking(request);
    }

    @Override
    public Observable<DeleteMedicineTakingEventsResponse> deleteMedicineTakingWithEvents(@NonNull DeleteMedicineTakingEventsRequest request) {
        return cleanUpDbService.deleteMedicineTakingEvents(request);
    }

    @Override
    public Observable<CompleteMedicineTakingResponse> completeMedicineTaking(@NonNull CompleteMedicineTakingRequest request) {
        return cleanUpDbService.completeMedicineTaking(request);
    }

    @Override
    public Observable<Integer> continueLinearGroup(@NonNull MedicineTaking medicineTaking,
                                                   @NonNull LocalDate sinceDate,
                                                   @NonNull Integer linearGroup) {
        return medicineTakingDbService.continueLinearGroup(medicineTaking, sinceDate, linearGroup);
    }
}
