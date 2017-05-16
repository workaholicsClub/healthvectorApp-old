package ru.android.childdiary.data.repositories.medical;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
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

@Singleton
public class MedicineTakingDataRepository implements MedicineTakingRepository {
    private final MedicineTakingDbService dbService;
    private final CleanUpDbService cleanUpDbService;

    @Inject
    public MedicineTakingDataRepository(MedicineTakingDbService dbService,
                                        CleanUpDbService cleanUpDbService) {
        this.dbService = dbService;
        this.cleanUpDbService = cleanUpDbService;
    }

    @Override
    public Observable<List<Medicine>> getMedicines() {
        return dbService.getMedicines();
    }

    @Override
    public Observable<Medicine> addMedicine(@NonNull Medicine medicine) {
        return dbService.addMedicine(medicine);
    }

    @Override
    public Observable<Medicine> deleteMedicine(@NonNull Medicine medicine) {
        return dbService.deleteMedicine(medicine);
    }

    @Override
    public Observable<List<MedicineMeasure>> getMedicineMeasureList() {
        return dbService.getMedicineMeasureList();
    }

    @Override
    public Observable<GetMedicineTakingListResponse> getMedicineTakingList(@NonNull GetMedicineTakingListRequest request) {
        return dbService.getMedicineTakingList(request);
    }

    @Override
    public Observable<MedicineTaking> addMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return dbService.add(medicineTaking);
    }

    @Override
    public Observable<MedicineTaking> updateMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return dbService.update(medicineTaking);
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
