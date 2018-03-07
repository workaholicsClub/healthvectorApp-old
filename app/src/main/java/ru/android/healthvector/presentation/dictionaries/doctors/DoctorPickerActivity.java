package ru.android.healthvector.presentation.dictionaries.doctors;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import lombok.Getter;
import ru.android.healthvector.R;
import ru.android.healthvector.data.types.Sex;
import ru.android.healthvector.di.ApplicationComponent;
import ru.android.healthvector.domain.dictionaries.doctors.data.Doctor;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.healthvector.presentation.dictionaries.core.BaseAddActivity;
import ru.android.healthvector.presentation.dictionaries.core.BaseAddView;
import ru.android.healthvector.presentation.dictionaries.core.BasePickerActivity;
import ru.android.healthvector.presentation.dictionaries.doctors.adapters.DoctorActionListener;
import ru.android.healthvector.presentation.dictionaries.doctors.adapters.DoctorAdapter;
import ru.android.healthvector.utils.ui.ThemeUtils;

public class DoctorPickerActivity extends BasePickerActivity<Doctor, DoctorPickerView>
        implements DoctorPickerView, DoctorActionListener {
    @Getter
    @InjectPresenter
    DoctorPickerPresenter presenter;

    public static Intent getIntent(Context context, @Nullable Sex sex, boolean pick) {
        return new Intent(context, DoctorPickerActivity.class)
                .putExtra(ExtraConstants.EXTRA_SEX, sex)
                .putExtra(ExtraConstants.EXTRA_PICK, pick);
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
    public void deletionRestricted() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.deletion_restricted_doctor)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    protected Class<? extends BaseAddActivity<Doctor, ? extends BaseAddView<Doctor>>> getAddActivityClass() {
        return DoctorAddActivity.class;
    }

    @Override
    protected BaseRecyclerViewAdapter<Doctor, ? extends BaseRecyclerViewHolder<Doctor>> createAdapter() {
        return new DoctorAdapter(this, this, this);
    }

    @Override
    protected int getMaxLength() {
        return maxLengthName;
    }

    @Override
    protected String getIntentionText() {
        return getString(R.string.add_doctor);
    }

    @Override
    protected int getDeleteConfirmationStringId() {
        return R.string.delete_doctor_confirmation_dialog_title;
    }
}
