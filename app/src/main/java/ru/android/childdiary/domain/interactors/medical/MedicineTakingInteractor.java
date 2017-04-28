package ru.android.childdiary.domain.interactors.medical;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.data.repositories.medical.MedicineTakingDataRepository;
import ru.android.childdiary.domain.core.Interactor;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;

public class MedicineTakingInteractor implements Interactor {
    private final MedicineTakingRepository medicineTakingRepository;

    @Inject
    public MedicineTakingInteractor(MedicineTakingDataRepository medicineTakingRepository) {
        this.medicineTakingRepository = medicineTakingRepository;
    }

    public Observable<List<Medicine>> getMedicines() {
        return medicineTakingRepository.getMedicines();
    }

    public Observable<Medicine> addMedicine(@NonNull Medicine medicine) {
        return medicineTakingRepository.addMedicine(medicine);
    }

    public Observable<List<MedicineTaking>> getMedicineTakingList() {
        return medicineTakingRepository.getMedicineTakingList();
    }

    public Observable<MedicineTaking> addMedicineTaking(@NonNull MedicineTaking medicineTaking) {
        return medicineTakingRepository.addMedicineTaking(medicineTaking);
    }
}
