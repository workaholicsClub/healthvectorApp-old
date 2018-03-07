package ru.android.healthvector.presentation.dictionaries.medicines;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryFieldType;
import ru.android.healthvector.domain.dictionaries.medicines.MedicineInteractor;
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.presentation.dictionaries.core.BaseAddPresenter;

@InjectViewState
public class MedicineAddPresenter extends BaseAddPresenter<Medicine, MedicineAddView> {
    @Getter
    @Inject
    MedicineInteractor interactor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected DictionaryFieldType getNameFieldType() {
        return DictionaryFieldType.MEDICINE_NAME;
    }
}
