package ru.android.childdiary.data.repositories.medical;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.android.childdiary.domain.interactors.medical.MedicineTaking;
import ru.android.childdiary.domain.interactors.medical.MedicineTakingRepository;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

@Singleton
public class MedicineTakingDataRepository implements MedicineTakingRepository {
    private final MedicineTakingDbService dbService;

    public MedicineTakingDataRepository(MedicineTakingDbService dbService) {
        this.dbService = dbService;
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
    public Observable<List<MedicineTaking>> getMedicineTakingList() {
        return dbService.getMedicineTakingList();
    }

    @Override
    public Observable<Medicine> addMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return dbService.addMedicineTaking(medicineTaking);
    }
}
