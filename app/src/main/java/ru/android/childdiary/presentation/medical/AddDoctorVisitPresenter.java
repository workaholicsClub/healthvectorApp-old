package ru.android.childdiary.presentation.medical;

import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.presentation.core.BasePresenter;

public class AddDoctorVisitPresenter extends BasePresenter<AddDoctorVisitView> {
    @Override
    protected void injectPresenter(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }
}
