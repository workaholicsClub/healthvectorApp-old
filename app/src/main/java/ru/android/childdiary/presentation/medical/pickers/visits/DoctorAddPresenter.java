package ru.android.childdiary.presentation.medical.pickers.visits;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.medical.pickers.core.BaseAddPresenter;

@InjectViewState
public class DoctorAddPresenter extends BaseAddPresenter<Doctor, DoctorAddView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
