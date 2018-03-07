package ru.android.healthvector.presentation.dictionaries.doctors;

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
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.dictionaries.core.BaseAddActivity;
import ru.android.healthvector.presentation.medical.adapters.core.StringFilteredAdapter;

public class DoctorAddActivity extends BaseAddActivity<Doctor, DoctorAddView> implements DoctorAddView {
    @Getter
    @InjectPresenter
    DoctorAddPresenter presenter;

    public static Intent getIntent(Context context, @Nullable Sex sex, @Nullable String defaultText) {
        return new Intent(context, DoctorAddActivity.class)
                .putExtra(ExtraConstants.EXTRA_SEX, sex)
                .putExtra(ExtraConstants.EXTRA_TEXT, defaultText);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.add_doctor);
    }

    @Override
    @LayoutRes
    protected int getContentLayoutResourceId() {
        return R.layout.activity_add_doctor;
    }

    @Override
    protected Doctor buildItem() {
        String text = autoCompleteView.getText();
        return Doctor.builder().nameUser(text).build();
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
