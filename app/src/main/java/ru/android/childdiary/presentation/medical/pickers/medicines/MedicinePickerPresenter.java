package ru.android.childdiary.presentation.medical.pickers.medicines;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.MedicineTakingInteractor;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.presentation.medical.pickers.core.BasePickerPresenter;

@InjectViewState
public class MedicinePickerPresenter extends BasePickerPresenter<Medicine, MedicinePickerView> {
    @Inject
    MedicineTakingInteractor medicineTakingInteractor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected Observable<List<Medicine>> getAllItemsLoader() {
        return medicineTakingInteractor.getMedicines();
    }

    @Override
    protected boolean filter(@NonNull Medicine item, @Nullable String filter) {
        return item.getName() != null
                && (TextUtils.isEmpty(filter)
                || item.getName().toLowerCase().contains(filter.toLowerCase()));
    }

    @Override
    protected Observable<Medicine> deleteItemLoader(@NonNull Medicine item) {
        return medicineTakingInteractor.deleteMedicine(item);
    }
}
