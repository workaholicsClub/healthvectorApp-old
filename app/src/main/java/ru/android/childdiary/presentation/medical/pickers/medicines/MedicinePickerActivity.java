package ru.android.childdiary.presentation.medical.pickers.medicines;

import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.R;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.presentation.medical.pickers.adapters.medicines.MedicineActionListener;
import ru.android.childdiary.presentation.medical.pickers.adapters.medicines.MedicineAdapter;
import ru.android.childdiary.presentation.medical.pickers.core.BaseAddActivity;
import ru.android.childdiary.presentation.medical.pickers.core.BaseAddView;
import ru.android.childdiary.presentation.medical.pickers.core.BasePickerActivity;

public class MedicinePickerActivity extends BasePickerActivity<Medicine, MedicinePickerView>
        implements MedicinePickerView, MedicineActionListener {
    @InjectPresenter
    MedicinePickerPresenter presenter;

    @Override
    protected void injectActivity(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        setupToolbarTitle(R.string.medicines_title);
    }

    @Override
    public MedicinePickerPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected Class<? extends BaseAddActivity<Medicine, ? extends BaseAddView<Medicine>>> getAddActivityClass() {
        return MedicineAddActivity.class;
    }

    @Override
    protected BaseRecyclerViewAdapter<Medicine, ? extends BaseRecyclerViewHolder<Medicine>> createAdapter() {
        return new MedicineAdapter(this, this, this);
    }
}
