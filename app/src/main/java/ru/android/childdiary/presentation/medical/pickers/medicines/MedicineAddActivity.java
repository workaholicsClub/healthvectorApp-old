package ru.android.childdiary.presentation.medical.pickers.medicines;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.presentation.medical.pickers.core.BaseAddActivity;

public class MedicineAddActivity extends BaseAddActivity<Medicine, MedicineAddView> implements MedicineAddView {
    @InjectPresenter
    MedicineAddPresenter presenter;

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView.setImageResource(R.drawable.ic_medicine);
        textView.setHint(R.string.add_medicine_hint);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.add_medicine_title);
    }
}
