package ru.android.childdiary.presentation.medical.pickers.visits;

import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.medical.pickers.core.BaseAddActivity;

public class DoctorAddActivity extends BaseAddActivity<Doctor, DoctorAddView> implements DoctorAddView {
    @InjectPresenter
    DoctorAddPresenter presenter;

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.add_doctor_title);
    }
}
