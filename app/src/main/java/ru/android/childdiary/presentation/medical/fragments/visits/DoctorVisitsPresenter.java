package ru.android.childdiary.presentation.medical.fragments.visits;

import com.arellomobile.mvp.InjectViewState;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.AppPartitionPresenter;

@InjectViewState
public class DoctorVisitsPresenter extends AppPartitionPresenter<DoctorVisitsView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    public void requestDoctorVisitDetail() {
        getViewState().navigateToDoctorVisit();
    }
}
