package ru.android.childdiary.presentation.medical.pickers.medicines;

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
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.add_medicine_title);
    }
}
