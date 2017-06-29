package ru.android.childdiary.presentation.medical.pickers.medicines;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.medical.core.Medicine;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.presentation.medical.pickers.adapters.medicines.MedicineActionListener;
import ru.android.childdiary.presentation.medical.pickers.adapters.medicines.MedicineAdapter;
import ru.android.childdiary.presentation.medical.pickers.core.BaseAddActivity;
import ru.android.childdiary.presentation.medical.pickers.core.BaseAddView;
import ru.android.childdiary.presentation.medical.pickers.core.BasePickerActivity;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class MedicinePickerActivity extends BasePickerActivity<Medicine, MedicinePickerView>
        implements MedicinePickerView, MedicineActionListener {
    @InjectPresenter
    MedicinePickerPresenter presenter;

    public static Intent getIntent(Context context, @Nullable Sex sex) {
        return new Intent(context, MedicinePickerActivity.class)
                .putExtra(ExtraConstants.EXTRA_SEX, sex);
    }

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
    public void delete(Medicine medicine) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.delete_medicine_confirmation_dialog_title)
                .setPositiveButton(R.string.delete,
                        (DialogInterface dialog, int which) -> getPresenter().deleteItem(medicine))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void deletionRestricted() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.deletion_restricted_medicine)
                .setPositiveButton(R.string.ok, null)
                .show();
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
