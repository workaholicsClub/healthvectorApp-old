package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.domain.interactors.child.data.Child;
import ru.android.childdiary.domain.interactors.dictionaries.medicines.Medicine;
import ru.android.childdiary.domain.interactors.dictionaries.medicinemeasure.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.data.MedicineTaking;
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

public interface MedicineTakingRepository {
    Observable<List<Medicine>> getMedicines();

    Observable<Medicine> addMedicine(@NonNull Medicine medicine);

    Observable<Medicine> deleteMedicine(@NonNull Medicine medicine);

    Observable<List<MedicineMeasure>> getMedicineMeasureList();

    Single<Boolean> hasConnectedEvents(@NonNull MedicineTaking medicineTaking);

    Single<Boolean> hasDataToFilter(@NonNull Child child);

    Observable<GetMedicineTakingListResponse> getMedicineTakingList(@NonNull GetMedicineTakingListRequest request);

    Observable<UpsertMedicineTakingResponse> addMedicineTaking(@NonNull UpsertMedicineTakingRequest request);

    Observable<UpsertMedicineTakingResponse> updateMedicineTaking(@NonNull UpsertMedicineTakingRequest request);

    Observable<DeleteMedicineTakingResponse> deleteMedicineTaking(@NonNull DeleteMedicineTakingRequest request);

    Observable<DeleteMedicineTakingEventsResponse> deleteMedicineTakingWithEvents(@NonNull DeleteMedicineTakingEventsRequest request);

    Observable<CompleteMedicineTakingResponse> completeMedicineTaking(@NonNull CompleteMedicineTakingRequest request);
}
