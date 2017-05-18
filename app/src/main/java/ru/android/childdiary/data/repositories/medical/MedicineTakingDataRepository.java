package ru.android.childdiary.data.repositories.medical;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.data.repositories.core.CleanUpDbService;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.MedicineTakingRepository;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.requests.CompleteMedicineTakingRequest;
import ru.android.childdiary.domain.interactors.medical.requests.CompleteMedicineTakingResponse;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingEventsRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingEventsResponse;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingRequest;
import ru.android.childdiary.domain.interactors.medical.requests.DeleteMedicineTakingResponse;
import ru.android.childdiary.domain.interactors.medical.requests.GetMedicineTakingListRequest;
import ru.android.childdiary.domain.interactors.medical.requests.GetMedicineTakingListResponse;
import ru.android.childdiary.domain.interactors.medical.requests.UpsertMedicineTakingRequest;
import ru.android.childdiary.domain.interactors.medical.requests.UpsertMedicineTakingResponse;

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
    public Observable<List<Medicine>> getMedicines() {
        return medicineTakingDbService.getMedicines();
    }

    @Override
    public Observable<Medicine> addMedicine(@NonNull Medicine medicine) {
        return medicineTakingDbService.addMedicine(medicine);
    }

    @Override
    public Observable<Medicine> deleteMedicine(@NonNull Medicine medicine) {
        return medicineTakingDbService.deleteMedicine(medicine);
    }

    @Override
    public Observable<List<MedicineMeasure>> getMedicineMeasureList() {
        return medicineTakingDbService.getMedicineMeasureList();
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
}
