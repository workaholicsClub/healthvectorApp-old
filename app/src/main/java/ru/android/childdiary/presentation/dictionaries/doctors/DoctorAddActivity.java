package ru.android.childdiary.presentation.dictionaries.doctors;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.tokenautocomplete.FilteredArrayAdapter;

import java.util.List;

import io.reactivex.Observable;
import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.dictionaries.doctors.data.Doctor;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.medical.adapters.core.StringFilteredAdapter;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddActivity;

public class DoctorAddActivity extends BaseAddActivity<Doctor, DoctorAddView> implements DoctorAddView {
    @Getter
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
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.add_doctor_title);
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_add_doctor;
    }

    @Override
    protected Doctor buildItem() {
        String text = autoCompleteView.getText();
        return Doctor.builder().name(text).build();
    }

    @Override
    protected FilteredArrayAdapter<String> createFilteredAdapter(@NonNull List<Doctor> items) {
        return new StringFilteredAdapter(this, Observable.fromIterable(items)
                .filter(item -> !TextUtils.isEmpty(item.getName()))
                .map(Doctor::getName)
                .toList()
                .blockingGet(), StringFilteredAdapter.FilterType.CONTAINS);
    }
}
