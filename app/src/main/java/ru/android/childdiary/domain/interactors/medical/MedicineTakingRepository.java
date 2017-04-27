package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import ru.android.childdiary.domain.core.Repository;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

public interface MedicineTakingRepository extends Repository {
    Observable<List<Medicine>> getMedicines();

    Observable<Medicine> addMedicine(@NonNull Medicine medicine);

    Observable<List<MedicineTaking>> getMedicineTakingList();

    Observable<Medicine> addMedicineTaking(@NonNull MedicineTaking medicineTaking);
}
