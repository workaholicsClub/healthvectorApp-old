package ru.android.childdiary.presentation.medical.pickers.visits;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.tokenautocomplete.FilteredArrayAdapter;

import java.util.List;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.medical.adapters.visits.DoctorFilteredAdapter;
import ru.android.childdiary.presentation.medical.pickers.core.BaseAddActivity;

public class DoctorAddActivity extends BaseAddActivity<Doctor, DoctorAddView> implements DoctorAddView {
    @InjectPresenter
    DoctorAddPresenter presenter;

    public static Intent getIntent(Context context, @Nullable Sex sex) {
        return new Intent(context, DoctorAddActivity.class)
                .putExtra(ExtraConstants.EXTRA_SEX, sex);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView.setImageResource(R.drawable.ic_doctor);
        textView.setHint(R.string.enter_doctor_name);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.add_doctor_title);
    }

    @Override
    public DoctorAddPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected Doctor buildItem() {
        String text = textView.getText().toString().trim();
        return Doctor.builder().name(text).build();
    }

    @Override
    protected FilteredArrayAdapter<Doctor> createFilteredAdapter(@NonNull List<Doctor> items) {
        return new DoctorFilteredAdapter(this, items);
    }
}
