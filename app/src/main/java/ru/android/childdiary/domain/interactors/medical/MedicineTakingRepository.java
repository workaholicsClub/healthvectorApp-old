package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.core.Repository;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.domain.interactors.medical.core.MedicineMeasure;
import ru.android.childdiary.domain.interactors.medical.requests.MedicineTakingListRequest;

public interface MedicineTakingRepository extends Repository {
    Observable<List<Medicine>> getMedicines();

    Observable<Medicine> addMedicine(@NonNull Medicine medicine);

    Observable<Medicine> deleteMedicine(@NonNull Medicine medicine);

    Observable<List<MedicineMeasure>> getMedicineMeasureList();

    Observable<List<MedicineTaking>> getMedicineTakingList(@NonNull MedicineTakingListRequest request);

    Observable<MedicineTaking> addMedicineTaking(@NonNull MedicineTaking medicineTaking);

    Observable<MedicineTaking> updateMedicineTaking(@NonNull MedicineTaking medicineTaking);

    Observable<MedicineTaking> deleteMedicineTaking(@NonNull MedicineTaking medicineTaking);
}
