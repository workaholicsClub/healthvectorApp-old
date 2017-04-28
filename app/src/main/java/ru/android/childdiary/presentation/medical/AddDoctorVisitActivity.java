package ru.android.childdiary.presentation.medical;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.core.BaseMvpActivity;
import ru.android.childdiary.presentation.core.fields.widgets.FieldDoctorView;

public class AddDoctorVisitActivity extends BaseMvpActivity implements AddDoctorVisitView {
    @InjectPresenter
    AddDoctorVisitPresenter presenter;

    @BindView(R.id.doctorView)
    FieldDoctorView doctorView;

    public static Intent getIntent(Context context) {
        return new Intent(context, AddDoctorVisitActivity.class);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor_visit);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_action_back);
    }

    @Override
    protected void themeChanged() {
        super.themeChanged();
        // TODO checkbox color
    }

    @Override
    public void showDoctors(List<Doctor> doctors) {
        doctorView.updateAdapter(doctors);
    }
}
