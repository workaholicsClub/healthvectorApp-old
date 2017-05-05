package ru.android.childdiary.presentation.medical.pickers.medicines;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.presentation.medical.pickers.core.BaseAddPresenter;

@InjectViewState
public class MedicineAddPresenter extends BaseAddPresenter<Medicine, MedicineAddView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
