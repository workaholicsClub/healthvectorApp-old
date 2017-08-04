package ru.android.childdiary.presentation.dictionaries.doctors;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import lombok.Getter;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.dictionaries.core.validation.DictionaryFieldType;
import ru.android.childdiary.domain.interactors.dictionaries.doctors.DoctorInteractor;
import ru.android.childdiary.domain.interactors.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddPresenter;

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
