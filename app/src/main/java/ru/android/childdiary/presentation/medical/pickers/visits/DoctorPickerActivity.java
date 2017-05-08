package ru.android.childdiary.presentation.medical.pickers.visits;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.core.Doctor;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.presentation.medical.pickers.adapters.doctors.DoctorActionListener;
import ru.android.childdiary.presentation.medical.pickers.adapters.doctors.DoctorAdapter;
import ru.android.childdiary.presentation.medical.pickers.core.BaseAddActivity;
import ru.android.childdiary.presentation.medical.pickers.core.BaseAddView;
import ru.android.childdiary.presentation.medical.pickers.core.BasePickerActivity;

public class DoctorPickerActivity extends BasePickerActivity<Doctor, DoctorPickerView>
        implements DoctorPickerView, DoctorActionListener {
    @InjectPresenter
    DoctorPickerPresenter presenter;

    public static Intent getIntent(Context context, @Nullable Sex sex) {
        return new Intent(context, DoctorPickerActivity.class)
                .putExtra(ExtraConstants.EXTRA_SEX, sex);
    }

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.doctors_title);
    }

    @Override
    public DoctorPickerPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected Class<? extends BaseAddActivity<Doctor, ? extends BaseAddView<Doctor>>> getAddActivityClass() {
        return DoctorAddActivity.class;
    }

    @Override
    protected BaseRecyclerViewAdapter<Doctor, ? extends BaseRecyclerViewHolder<Doctor>> createAdapter() {
        return new DoctorAdapter(this, this, this);
    }
}
