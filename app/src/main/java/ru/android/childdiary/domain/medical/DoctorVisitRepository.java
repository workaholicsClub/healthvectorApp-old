package ru.android.childdiary.domain.medical;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.android.childdiary.domain.calendar.data.core.LengthValue;
import ru.android.childdiary.domain.child.data.Child;
import ru.android.childdiary.domain.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.domain.medical.data.DoctorVisit;
import ru.android.childdiary.domain.medical.requests.CompleteDoctorVisitRequest;
import ru.android.childdiary.domain.medical.requests.CompleteDoctorVisitResponse;
import ru.android.childdiary.domain.medical.requests.DeleteDoctorVisitEventsRequest;
import ru.android.childdiary.domain.medical.requests.DeleteDoctorVisitEventsResponse;
import ru.android.childdiary.domain.medical.requests.DeleteDoctorVisitRequest;
import ru.android.childdiary.domain.medical.requests.DeleteDoctorVisitResponse;
import ru.android.childdiary.domain.medical.requests.GetDoctorVisitsRequest;
import ru.android.childdiary.domain.medical.requests.GetDoctorVisitsResponse;
import ru.android.childdiary.domain.medical.requests.UpsertDoctorVisitRequest;
import ru.android.childdiary.domain.medical.requests.UpsertDoctorVisitResponse;

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
