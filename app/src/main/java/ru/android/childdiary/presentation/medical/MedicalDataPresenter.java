package ru.android.childdiary.presentation.medical;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;

@InjectViewState
public class MedicalDataPresenter extends AppPartitionPresenter<MedicalDataView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void addDoctorVisit() {
        getViewState().navigateToDoctorVisitAdd();
    }

    public void addMedicineTaking() {
        getViewState().navigateToMedicineTakingAdd();
    }
}
