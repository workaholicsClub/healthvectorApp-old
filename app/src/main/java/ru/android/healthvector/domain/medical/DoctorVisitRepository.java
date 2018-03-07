package ru.android.healthvector.domain.medical;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.healthvector.domain.calendar.data.core.LengthValue;
import ru.android.healthvector.domain.child.data.Child;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;
import ru.android.healthvector.domain.medical.data.DoctorVisit;
import ru.android.healthvector.domain.medical.requests.CompleteDoctorVisitRequest;
import ru.android.healthvector.domain.medical.requests.CompleteDoctorVisitResponse;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitEventsRequest;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitEventsResponse;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitRequest;
import ru.android.healthvector.domain.medical.requests.DeleteDoctorVisitResponse;
import ru.android.healthvector.domain.medical.requests.GetDoctorVisitsRequest;
import ru.android.healthvector.domain.medical.requests.GetDoctorVisitsResponse;
import ru.android.healthvector.domain.medical.requests.UpsertDoctorVisitRequest;
import ru.android.healthvector.domain.medical.requests.UpsertDoctorVisitResponse;

public interface DoctorVisitRepository {
    Observable<Doctor> getLastDoctor();

    Doctor setLastDoctor(@Nullable Doctor doctor);

    Observable<GetDoctorVisitsResponse> getDoctorVisits(@NonNull GetDoctorVisitsRequest request);

    Single<Boolean> hasConnectedEvents(@NonNull DoctorVisit doctorVisit);

    Single<Boolean> hasDataToFilter(@NonNull Child child);

    Observable<UpsertDoctorVisitResponse> addDoctorVisit(@NonNull UpsertDoctorVisitRequest request);

    Observable<UpsertDoctorVisitResponse> updateDoctorVisit(@NonNull UpsertDoctorVisitRequest request);

    Observable<DeleteDoctorVisitResponse> deleteDoctorVisit(@NonNull DeleteDoctorVisitRequest request);

    Observable<DeleteDoctorVisitEventsResponse> deleteDoctorVisitWithEvents(@NonNull DeleteDoctorVisitEventsRequest request);

    Observable<CompleteDoctorVisitResponse> completeDoctorVisit(@NonNull CompleteDoctorVisitRequest request);

    Observable<Integer> continueLinearGroup(@NonNull DoctorVisit doctorVisit,
                                            @NonNull LocalDate sinceDate,
                                            @NonNull Integer linearGroup,
                                            @NonNull LengthValue lengthValue);
}
