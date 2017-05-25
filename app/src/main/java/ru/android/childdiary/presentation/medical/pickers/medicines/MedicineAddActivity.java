package ru.android.childdiary.presentation.medical.pickers.medicines;

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
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.medical.adapters.medicines.MedicineFilteredAdapter;
import ru.android.childdiary.presentation.medical.pickers.core.BaseAddActivity;
import ru.android.childdiary.presentation.medical.pickers.visits.DoctorAddActivity;

public class MedicineAddActivity extends BaseAddActivity<Medicine, MedicineAddView> implements MedicineAddView {
    @InjectPresenter
    MedicineAddPresenter presenter;

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
        imageView.setImageResource(R.drawable.ic_medicine);
        textView.setHint(R.string.enter_medicine_name);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.add_medicine_title);
    }

    @Override
    public MedicineAddPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected Medicine buildItem() {
        String text = textView.getText().toString().trim();
        return Medicine.builder().name(text).build();
    }

    @Override
    protected FilteredArrayAdapter<Medicine> createFilteredAdapter(@NonNull List<Medicine> items) {
        return new MedicineFilteredAdapter(this, items);
    }
}
