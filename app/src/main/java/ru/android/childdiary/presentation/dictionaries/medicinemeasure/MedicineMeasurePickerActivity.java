package ru.android.childdiary.presentation.dictionaries.medicinemeasure;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;

import lombok.Getter;
import ru.android.childdiary.R;
import ru.android.childdiary.data.types.Sex;
import ru.android.childdiary.di.ApplicationComponent;
import ru.android.childdiary.domain.interactors.dictionaries.medicinemeasure.data.MedicineMeasure;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddActivity;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddView;
import ru.android.childdiary.presentation.dictionaries.core.BasePickerActivity;
import ru.android.childdiary.presentation.dictionaries.medicinemeasure.adapters.MedicineMeasureActionListener;
import ru.android.childdiary.presentation.dictionaries.medicinemeasure.adapters.MedicineMeasureAdapter;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class MedicineMeasurePickerActivity extends BasePickerActivity<MedicineMeasure, MedicineMeasurePickerView>
        implements MedicineMeasurePickerView, MedicineMeasureActionListener {
    @Getter
    @InjectPresenter
    MedicineMeasurePickerPresenter presenter;

    public static Intent getIntent(Context context, @Nullable Sex sex, boolean pick) {
        return new Intent(context, MedicineMeasurePickerActivity.class)
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
        setupToolbarTitle(R.string.measure_units);
    }

    @Override
    public void delete(MedicineMeasure medicineMeasure) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.delete_measure_unit)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> getPresenter().deleteItem(medicineMeasure))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void deletionRestricted() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.deletion_restricted_medicine_measure)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    protected Class<? extends BaseAddActivity<MedicineMeasure, ? extends BaseAddView<MedicineMeasure>>> getAddActivityClass() {
        return MedicineMeasureAddActivity.class;
    }

    @Override
    protected BaseRecyclerViewAdapter<MedicineMeasure, ? extends BaseRecyclerViewHolder<MedicineMeasure>> createAdapter() {
        return new MedicineMeasureAdapter(this, this, this);
    }

    @Override
    protected String getIntentionText(boolean isFiltering) {
        return (isFiltering ? getString(R.string.nothing_found) + "\n\n" : "")
                + getString(R.string.intention_add_measure_unit);
    }
}
