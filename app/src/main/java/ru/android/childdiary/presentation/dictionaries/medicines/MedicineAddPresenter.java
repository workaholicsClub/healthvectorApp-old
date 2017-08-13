package ru.android.childdiary.presentation.dictionaries.medicines;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.dictionaries.core.validation.DictionaryFieldType;
import ru.android.childdiary.domain.dictionaries.medicines.MedicineInteractor;
import ru.android.childdiary.domain.dictionaries.medicines.data.Medicine;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddPresenter;

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
