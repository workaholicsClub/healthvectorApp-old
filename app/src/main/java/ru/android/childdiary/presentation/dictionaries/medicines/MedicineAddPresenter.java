package ru.android.childdiary.presentation.dictionaries.medicines;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.dictionaries.core.MedicalDictionaryInteractor;
import ru.android.childdiary.domain.interactors.medical.MedicineTakingInteractor;
import ru.android.childdiary.domain.interactors.dictionaries.medicines.Medicine;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddPresenter;

@InjectViewState
public class MedicineAddPresenter extends BaseAddPresenter<Medicine, MedicineAddView> {
    @Inject
    MedicineTakingInteractor medicineTakingInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    public void add(@NonNull Medicine item) {
        unsubscribeOnDestroy(medicineTakingInteractor.addMedicine(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::itemAdded, this::onUnexpectedError));
    }

    @Override
    protected Observable<List<Medicine>> getAllItemsLoader() {
        return medicineTakingInteractor.getMedicines();
    }

    @Override
    protected MedicalDictionaryInteractor<Medicine> getInteractor() {
        return medicineTakingInteractor;
    }
}
