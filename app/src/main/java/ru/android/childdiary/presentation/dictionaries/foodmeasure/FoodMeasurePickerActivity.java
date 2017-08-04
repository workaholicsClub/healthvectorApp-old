package ru.android.childdiary.presentation.dictionaries.foodmeasure;

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
import ru.android.childdiary.domain.interactors.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.childdiary.presentation.core.ExtraConstants;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.childdiary.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddActivity;
import ru.android.childdiary.presentation.dictionaries.core.BaseAddView;
import ru.android.childdiary.presentation.dictionaries.core.BasePickerActivity;
import ru.android.childdiary.presentation.dictionaries.foodmeasure.adapters.FoodMeasureActionListener;
import ru.android.childdiary.presentation.dictionaries.foodmeasure.adapters.FoodMeasureAdapter;
import ru.android.childdiary.utils.ui.ThemeUtils;

public class FoodMeasurePickerActivity extends BasePickerActivity<FoodMeasure, FoodMeasurePickerView>
        implements FoodMeasurePickerView, FoodMeasureActionListener {
    @Getter
    @InjectPresenter
    FoodMeasurePickerPresenter presenter;

    public static Intent getIntent(Context context, @Nullable Sex sex, boolean pick) {
        return new Intent(context, FoodMeasurePickerActivity.class)
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
    public void delete(FoodMeasure foodMeasure) {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.delete_measure_unit)
                .setPositiveButton(R.string.delete,
                        (dialog, which) -> getPresenter().deleteItem(foodMeasure))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void deletionRestricted() {
        new AlertDialog.Builder(this, ThemeUtils.getThemeDialogRes(getSex()))
                .setMessage(R.string.deletion_restricted_food_measure)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    @Override
    protected Class<? extends BaseAddActivity<FoodMeasure, ? extends BaseAddView<FoodMeasure>>> getAddActivityClass() {
        return FoodMeasureAddActivity.class;
    }

    @Override
    protected BaseRecyclerViewAdapter<FoodMeasure, ? extends BaseRecyclerViewHolder<FoodMeasure>> createAdapter() {
        return new FoodMeasureAdapter(this, this, this);
    }

    @Override
    protected String getIntentionText(boolean isFiltering) {
        return (isFiltering ? getString(R.string.nothing_found) + "\n\n" : "")
                + getString(R.string.intention_add_measure_unit);
    }
}
