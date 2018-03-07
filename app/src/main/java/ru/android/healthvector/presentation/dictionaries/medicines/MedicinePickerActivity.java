package ru.android.healthvector.presentation.dictionaries.medicines;

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
import ru.android.healthvector.domain.dictionaries.medicines.data.Medicine;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.healthvector.presentation.dictionaries.core.BaseAddActivity;
import ru.android.healthvector.presentation.dictionaries.core.BaseAddView;
import ru.android.healthvector.presentation.dictionaries.core.BasePickerActivity;
import ru.android.healthvector.presentation.dictionaries.medicines.adapters.MedicineActionListener;
import ru.android.healthvector.presentation.dictionaries.medicines.adapters.MedicineAdapter;
import ru.android.healthvector.utils.ui.ThemeUtils;

public class MedicinePickerActivity extends BasePickerActivity<Medicine, MedicinePickerView>
        implements MedicinePickerView, MedicineActionListener {
    @Getter
    @InjectPresenter
    MedicinePickerPresenter presenter;

    public static Intent getIntent(Context context, @Nullable Sex sex, boolean pick) {
        return new Intent(context, MedicinePickerActivity.class)
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
        setupToolbarTitle(R.string.medicines_title);
    }

    @Override
    public void deletionRestricted() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.deletion_restricted_medicine)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    protected Class<? extends BaseAddActivity<Medicine, ? extends BaseAddView<Medicine>>> getAddActivityClass() {
        return MedicineAddActivity.class;
    }

    @Override
    protected BaseRecyclerViewAdapter<Medicine, ? extends BaseRecyclerViewHolder<Medicine>> createAdapter() {
        return new MedicineAdapter(this, this, this);
    }

    @Override
    protected int getMaxLength() {
        return maxLengthName;
    }

    @Override
    protected String getIntentionText() {
        return getString(R.string.add_medicine);
    }

    @Override
    protected int getDeleteConfirmationStringId() {
        return R.string.delete_medicine_confirmation_dialog_title;
    }
}
