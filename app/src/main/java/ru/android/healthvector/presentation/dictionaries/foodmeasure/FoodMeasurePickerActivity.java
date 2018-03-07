package ru.android.healthvector.presentation.dictionaries.foodmeasure;

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
import ru.android.healthvector.domain.dictionaries.foodmeasure.data.FoodMeasure;
import ru.android.healthvector.presentation.core.ExtraConstants;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewAdapter;
import ru.android.healthvector.presentation.core.adapters.recycler.BaseRecyclerViewHolder;
import ru.android.healthvector.presentation.dictionaries.core.BaseAddActivity;
import ru.android.healthvector.presentation.dictionaries.core.BaseAddView;
import ru.android.healthvector.presentation.dictionaries.core.BasePickerActivity;
import ru.android.healthvector.presentation.dictionaries.foodmeasure.adapters.FoodMeasureActionListener;
import ru.android.healthvector.presentation.dictionaries.foodmeasure.adapters.FoodMeasureAdapter;
import ru.android.healthvector.utils.ui.ThemeUtils;

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
    protected int getMaxLength() {
        return maxLengthNameSmall;
    }

    @Override
    protected String getIntentionText() {
        return getString(R.string.add_measure_unit);
    }

    @Override
    protected int getDeleteConfirmationStringId() {
        return R.string.delete_measure_unit;
    }
}
