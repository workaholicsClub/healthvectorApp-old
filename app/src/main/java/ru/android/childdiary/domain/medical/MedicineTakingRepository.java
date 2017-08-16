package ru.android.childdiary.domain.medical;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.domain.child.data.Child;
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

public interface MedicineTakingRepository {
    Single<Boolean> hasConnectedEvents(@NonNull MedicineTaking medicineTaking);

    Single<Boolean> hasDataToFilter(@NonNull Child child);

    Observable<GetMedicineTakingListResponse> getMedicineTakingList(@NonNull GetMedicineTakingListRequest request);

    Observable<UpsertMedicineTakingResponse> addMedicineTaking(@NonNull UpsertMedicineTakingRequest request);

    Observable<UpsertMedicineTakingResponse> updateMedicineTaking(@NonNull UpsertMedicineTakingRequest request);

    Observable<DeleteMedicineTakingResponse> deleteMedicineTaking(@NonNull DeleteMedicineTakingRequest request);

    Observable<DeleteMedicineTakingEventsResponse> deleteMedicineTakingWithEvents(@NonNull DeleteMedicineTakingEventsRequest request);

    Observable<CompleteMedicineTakingResponse> completeMedicineTaking(@NonNull CompleteMedicineTakingRequest request);
}