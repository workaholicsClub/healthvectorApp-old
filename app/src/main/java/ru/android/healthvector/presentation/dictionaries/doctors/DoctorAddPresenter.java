package ru.android.healthvector.presentation.dictionaries.doctors;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.dictionaries.core.validation.DictionaryFieldType;
import ru.android.healthvector.domain.dictionaries.doctors.DoctorInteractor;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;
import ru.android.healthvector.presentation.dictionaries.core.BaseAddPresenter;

@InjectViewState
public class DoctorAddPresenter extends BaseAddPresenter<Doctor, DoctorAddView> {
    @Getter
    @Inject
    DoctorInteractor interactor;

    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected DictionaryFieldType getNameFieldType() {
        return DictionaryFieldType.DOCTOR_NAME;
    }
}
