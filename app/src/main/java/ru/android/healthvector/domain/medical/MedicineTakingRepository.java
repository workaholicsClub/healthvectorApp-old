package ru.android.healthvector.domain.medical;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.healthvector.domain.calendar.data.core.LengthValue;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.medical.data.MedicineTaking;
import ru.android.healthvector.domain.medical.requests.CompleteMedicineTakingRequest;
import ru.android.healthvector.domain.medical.requests.CompleteMedicineTakingResponse;
import ru.android.healthvector.domain.medical.requests.DeleteMedicineTakingEventsRequest;
import ru.android.healthvector.domain.medical.requests.DeleteMedicineTakingEventsResponse;
import ru.android.healthvector.domain.medical.requests.DeleteMedicineTakingRequest;
import ru.android.healthvector.domain.medical.requests.DeleteMedicineTakingResponse;
import ru.android.healthvector.domain.medical.requests.GetMedicineTakingListRequest;
import ru.android.healthvector.domain.medical.requests.GetMedicineTakingListResponse;
import ru.android.healthvector.domain.medical.requests.UpsertMedicineTakingRequest;
import ru.android.healthvector.domain.medical.requests.UpsertMedicineTakingResponse;

public interface MedicineTakingRepository {
    Single<Boolean> hasConnectedEvents(@NonNull MedicineTaking medicineTaking);

    Single<Boolean> hasDataToFilter(@NonNull Child child);

    Observable<GetMedicineTakingListResponse> getMedicineTakingList(@NonNull GetMedicineTakingListRequest request);

    Observable<UpsertMedicineTakingResponse> addMedicineTaking(@NonNull UpsertMedicineTakingRequest request);

    Observable<UpsertMedicineTakingResponse> updateMedicineTaking(@NonNull UpsertMedicineTakingRequest request);

    Observable<DeleteMedicineTakingResponse> deleteMedicineTaking(@NonNull DeleteMedicineTakingRequest request);

    Observable<DeleteMedicineTakingEventsResponse> deleteMedicineTakingWithEvents(@NonNull DeleteMedicineTakingEventsRequest request);

    Observable<CompleteMedicineTakingResponse> completeMedicineTaking(@NonNull CompleteMedicineTakingRequest request);

    Observable<Integer> continueLinearGroup(@NonNull MedicineTaking medicineTaking,
                                            @NonNull LocalDate sinceDate,
                                            @NonNull Integer linearGroup,
                                            @NonNull LengthValue lengthValue);
}
